package com.dwp.services.tx.account;

import com.dwp.exceptions.ResourceNotFoundException;
import com.dwp.models.Account;
import com.dwp.models.Logging;
import com.dwp.models.TransactionDetail;
import com.dwp.utils.AppUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class AccountServiceImpl implements AccountService {
    private final Map<Long, Account> accountMap;
    private final Logging logger;

    private Map<Long, TransactionDetail> accountTransactionDetails;

    public AccountServiceImpl(Logging logger) {
        this.accountMap = new HashMap<>();
        this.logger = logger;
        this.accountTransactionDetails = new HashMap<>();
    }

    @Override
    public void createAccount() {
        Account account = new Account();
        accountMap.put(account.getId(), account);
        logger.log(String.format("Account %s has been created successfully", account.getId()));
    }

    @Override
    public Account getOrThrowByAccountNumber(Long accountNumber) {
        if (accountMap.get(accountNumber) == null)
            throw new ResourceNotFoundException(AppUtil.RESOURCE_NOT_FOUND.apply("Account", "account number"));
        return accountMap.get(accountNumber);
    }

    @Override
    public void getAccountStatement(Long accountNumber) {
        TransactionDetail txDetails = accountTransactionDetails.get(accountNumber);
        if (txDetails == null) {
            logger.log("No Transaction done yet");
            return;
        }
        txDetails.getTransactions().forEach(tx -> {
            String d = String.format("From Account %s \n To Account %s \n Amount %s \n Date %s \n", tx.getFromAccount(), tx.getToAccount(), tx.getAmount(), tx.getTxDate());
            logger.log(d);
        });
    }

    @Override
    public void getAccountBalance(Long accountNumber) {
        logger.info("Account Balance ", accountMap.get(accountNumber).getBalance());
    }

    @Override
    public TransactionDetail getTxDetails(Long accountNumber) {
        return accountTransactionDetails.getOrDefault(accountNumber, new TransactionDetail());
    }

    @Override
    public void addTransaction(Long accountNumber, TransactionDetail transactionDetails) {
        this.accountTransactionDetails.put(accountNumber, transactionDetails);
    }

    @Override
    public void getAllAccountBalance() {
        StringBuilder sb = new StringBuilder();

        accountMap.values().forEach((account) -> {
            sb.append(String.format("Account Number %s :: Balance %s ", account.getId(), account.getBalance()));
            sb.append("\n");

        });
        logger.info("AccountBalance", sb.toString());
    }
}
