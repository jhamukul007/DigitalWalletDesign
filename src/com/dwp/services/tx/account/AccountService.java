package com.dwp.services.tx.account;

import com.dwp.models.Account;
import com.dwp.models.Transaction;
import com.dwp.models.TransactionDetail;

public interface AccountService {
    void createAccount();
    Account getOrThrowByAccountNumber(Long accountNumber);
    void getAccountStatement(Long accountNumber);
    void getAccountBalance(Long accountNumber);

    TransactionDetail getTxDetails(Long accountNumber);

    void addTransaction(Long accountNumber, TransactionDetail transactionDetails);

    void getAllAccountBalance();
}
