package com.dwp.services.tx.impl;

import com.dwp.dtos.PaymentRequestDto;
import com.dwp.enums.TransactionStatus;
import com.dwp.enums.TransactionType;
import com.dwp.exceptions.InsufficientBalanceException;
import com.dwp.exceptions.TransactionException;
import com.dwp.models.Logging;
import com.dwp.models.Transaction;
import com.dwp.models.TransactionDetail;
import com.dwp.models.User;
import com.dwp.services.UserService;
import com.dwp.services.tx.TransactionService;
import com.dwp.utils.AppUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionServiceImpl implements TransactionService {
    private final UserService userService;
    private final Logging logging;
    private ConcurrentHashMap<Long, TransactionStatus> transactionProcess;
    private Map<Long, TransactionDetail> userLevelTransactionDetails;

    public TransactionServiceImpl(UserService userService, Logging logging) {
        this.userService = userService;
        this.logging = logging;
        this.transactionProcess = new ConcurrentHashMap<>();
        this.userLevelTransactionDetails = new HashMap<>();
    }

    @Override
    public void pay(PaymentRequestDto paymentDto) {

        User senderUser = userService.getUserByIdOrThrowException(paymentDto.getFromUserId());
        validateBalance(senderUser, paymentDto.getAmount());
        User receiverUser = userService.getUserByIdOrThrowException(paymentDto.getToUserId());
        // Tx started
        if (transactionProcess.get(senderUser.getId()) == TransactionStatus.PENDING)
            throw new TransactionException("Transaction in progress");
        transactionProcess.put(senderUser.getId(), TransactionStatus.PENDING);
        logging.log("Transcation started ");
        TransactionDetail transactionDetail = userLevelTransactionDetails.getOrDefault(senderUser.getId(), new TransactionDetail());
        Transaction transaction = Transaction.builder()
                .id(AppUtil.getRandomId())
                .fromUserId(senderUser.getId())
                .toUserId(receiverUser.getId())
                .amount(paymentDto.getAmount())
                .transactionStatus(TransactionStatus.PENDING)
                .transactionType(TransactionType.DEBIT)
                .build();
        transactionDetail.addTransaction(transaction);
        userLevelTransactionDetails.put(senderUser.getId(), transactionDetail);
        try {
            senderUser.getDigitalWallet().debitAmount(paymentDto.getAmount());
        } catch (Exception e) {
            if (e instanceof InsufficientBalanceException)
                throw e;
            logging.log("Getting error while debit amount from user " + senderUser.getId());
            senderUser.getDigitalWallet().creditAmount(paymentDto.getAmount());
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionProcess.remove(senderUser.getId());
            return;
        }
        try {
            receiverUser.getDigitalWallet().creditAmount(paymentDto.getAmount());
        } catch (Exception e) {
            logging.log("Getting error while credit amount from user " + receiverUser.getId());
            senderUser.getDigitalWallet().creditAmount(paymentDto.getAmount());
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionProcess.remove(senderUser.getId());
            return;
        }

        TransactionDetail commitedTransaction = userLevelTransactionDetails.get(senderUser.getId());
        transaction = commitedTransaction.getTransactionById(transaction.getId());

        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        userLevelTransactionDetails.put(senderUser.getId(), transactionDetail);

        TransactionDetail creditTxDetail = userLevelTransactionDetails.getOrDefault(receiverUser.getId(), new TransactionDetail());
        Transaction creditTx = transaction.clone();
        creditTx.setTransactionType(TransactionType.CREDIT);
        creditTxDetail.addTransaction(creditTx);
        userLevelTransactionDetails.put(receiverUser.getId(), creditTxDetail);

        transactionProcess.remove(senderUser.getId());
    }

    void validateBalance(User user, BigDecimal amount) {
        int val = user.getDigitalWallet().getBalance().compareTo(amount);
        if (val < 0)
            throw new InsufficientBalanceException("Insufficient Balance " + amount.intValue());
    }

    @Override
    public TransactionDetail accountStatement(Long userId) {
        return userLevelTransactionDetails.get(userId);
    }

    @Override
    public void addFund(PaymentRequestDto fundDetails) {
        User receiverUser = userService.getUserByIdOrThrowException(fundDetails.getToUserId());
        TransactionDetail transactionDetail = userLevelTransactionDetails.getOrDefault(receiverUser.getId(), new TransactionDetail());
        Transaction transaction = Transaction.builder()
                .id(AppUtil.getRandomId())
                .fromUserId(fundDetails.getFromUserId())
                .toUserId(receiverUser.getId())
                .amount(fundDetails.getAmount())
                .transactionStatus(TransactionStatus.PENDING)
                .transactionType(TransactionType.CREDIT)
                .build();
        transactionDetail.addTransaction(transaction);
        userLevelTransactionDetails.put(receiverUser.getId(), transactionDetail);

        receiverUser.getDigitalWallet().creditAmount(fundDetails.getAmount());

        transactionDetail = userLevelTransactionDetails.get(transaction.getToUserId());
        transaction = transactionDetail.getTransactionById(transaction.getId());
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
    }


}
