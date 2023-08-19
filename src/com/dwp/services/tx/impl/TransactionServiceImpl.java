package com.dwp.services.tx.impl;

import com.dwp.dtos.PaymentRequestDto;
import com.dwp.enums.TransactionStatus;
import com.dwp.enums.TransactionType;
import com.dwp.exceptions.InsufficientBalanceException;
import com.dwp.exceptions.TransactionException;
import com.dwp.models.Account;
import com.dwp.models.Logging;
import com.dwp.models.Transaction;
import com.dwp.models.TransactionDetail;
import com.dwp.services.tx.TransactionService;
import com.dwp.services.tx.account.AccountService;
import com.dwp.utils.AppUtil;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionServiceImpl implements TransactionService {
    private final Logging logging;

    private final AccountService accountService;
    private ConcurrentHashMap<Long, TransactionStatus> transactionProcessForAccount;

    public TransactionServiceImpl(Logging logging, AccountService accountService) {
        this.logging = logging;
        this.accountService = accountService;
        this.transactionProcessForAccount = new ConcurrentHashMap<>();
    }

    @Override
    public void pay(PaymentRequestDto paymentDto) {

        Account senderAccount = accountService.getOrThrowByAccountNumber(paymentDto.getFromAccount());
        validateBalance(senderAccount, paymentDto.getAmount());
        Account receiverAccount = accountService.getOrThrowByAccountNumber(paymentDto.getToAccount());
        // Tx started
        if (transactionProcessForAccount.get(senderAccount.getId()) == TransactionStatus.PENDING)
            throw new TransactionException("Transaction in progress");
        transactionProcessForAccount.put(senderAccount.getId(), TransactionStatus.PENDING);
        logging.log("Transaction started ");

        TransactionDetail transactionDetail = accountService.getTxDetails(senderAccount.getId());

        Transaction transaction = Transaction.builder()
                .id(AppUtil.getRandomId())
                .fromAccount(senderAccount.getId())
                .toAccount(receiverAccount.getId())
                .amount(paymentDto.getAmount())
                .transactionStatus(TransactionStatus.PENDING)
                .transactionType(TransactionType.DEBIT)
                .build();
        transactionDetail.addTransaction(transaction);
        accountService.addTransaction(senderAccount.getId(), transactionDetail);
        try {
            senderAccount.debitAmount(paymentDto.getAmount());
        } catch (Exception e) {
            if (e instanceof InsufficientBalanceException)
                throw e;
            logging.log("Getting error while debit amount from account " + senderAccount.getId());
            senderAccount.creditAmount(paymentDto.getAmount());
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionProcessForAccount.remove(senderAccount.getId());
            return;
        }
        try {
            receiverAccount.creditAmount(paymentDto.getAmount());
        } catch (Exception e) {
            logging.log("Getting error while credit amount to account " + receiverAccount.getId());
            senderAccount.creditAmount(paymentDto.getAmount());
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionProcessForAccount.remove(senderAccount.getId());
            return;
        }

        TransactionDetail commitedTransaction = accountService.getTxDetails(senderAccount.getId());
        transaction = commitedTransaction.getTransactionById(transaction.getId());

        transaction.setTransactionStatus(TransactionStatus.COMPLETED);

        TransactionDetail creditTxDetail = accountService.getTxDetails(receiverAccount.getId());
        Transaction creditTx = transaction.clone();
        creditTx.setTransactionType(TransactionType.CREDIT);
        creditTxDetail.addTransaction(creditTx);
        accountService.addTransaction(receiverAccount.getId(), creditTxDetail);

        transactionProcessForAccount.remove(senderAccount.getId());

        logging.info("TX", String.format("Amount %s has been successfully transferred to user %s from user %s ", paymentDto.getAmount(),
                receiverAccount.getId(), senderAccount.getId()));
    }

    void validateBalance(Account account, BigDecimal amount) {
        int val = account.getBalance().compareTo(amount);
        if (val < 0)
            throw new InsufficientBalanceException("Insufficient Balance " + amount.intValue());
    }

    @Override
    public void addFund(PaymentRequestDto fundDetails) {
        Account receiverAccount = accountService.getOrThrowByAccountNumber(fundDetails.getToAccount());
        TransactionDetail transactionDetail = accountService.getTxDetails(receiverAccount.getId());
        Transaction transaction = Transaction.builder()
                .id(AppUtil.getRandomId())
                .fromAccount(fundDetails.getFromAccount())
                .toAccount(fundDetails.getToAccount())
                .amount(fundDetails.getAmount())
                .transactionStatus(TransactionStatus.PENDING)
                .transactionType(TransactionType.CREDIT)
                .build();
        transactionDetail.addTransaction(transaction);
        accountService.addTransaction(receiverAccount.getId(), transactionDetail);

        receiverAccount.creditAmount(fundDetails.getAmount());

        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        logging.info("FUND", String.format("Fund %s has been successfully added to account %s ", fundDetails.getAmount(),
                receiverAccount.getId()));
    }


}
