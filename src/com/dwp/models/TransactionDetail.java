package com.dwp.models;

import com.dwp.enums.TransactionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransactionDetail {
    private List<Transaction> transactions;

    public TransactionDetail() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction tx) {
        getTransactions().add(tx);
    }

    private List<Transaction> getTransactions() {
        return this.transactions;
    }

    public List<Transaction> getAllCompletedTransactions() {
        return transactions.stream().filter(tx -> Objects.deepEquals(tx.getTransactionStatus(), TransactionStatus.COMPLETED)).collect(Collectors.toList());
    }

    public List<Transaction> getAllFailedTransactions() {
        return transactions.stream().filter(tx -> Objects.deepEquals(tx.getTransactionStatus(), TransactionStatus.FAILED)).collect(Collectors.toList());
    }

    public List<Transaction> getAllPendingTransactions() {
        return transactions.stream().filter(tx -> Objects.deepEquals(tx.getTransactionStatus(), TransactionStatus.PENDING)).collect(Collectors.toList());
    }

    public Transaction getTransactionById(Long id) {
        return transactions.stream().filter(tx -> Objects.deepEquals(tx.getId(), id)).findAny().orElse(null);
    }

    @Override
    public String toString() {
        return "TransactionDetail{" +
                "transactions=" + transactions +
                '}';
    }
}
