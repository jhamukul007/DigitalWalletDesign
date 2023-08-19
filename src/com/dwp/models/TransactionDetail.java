package com.dwp.models;

import com.dwp.enums.TransactionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TransactionDetail {
    private Set<Transaction> transactions;

    public TransactionDetail() {
        this.transactions = new TreeSet<>(((o1, o2) -> o2.getTxDate().compareTo(o1.getTxDate())));
    }

    public void addTransaction(Transaction tx) {
        getTransactions().add(tx);
    }

    public Set<Transaction> getTransactions() {
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
