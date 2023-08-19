package com.dwp.models;

import com.dwp.enums.Status;
import com.dwp.exceptions.InsufficientBalanceException;
import com.dwp.utils.AppUtil;

import java.math.BigDecimal;

public class Account extends BaseEntity {
    private Status status;
    private BigDecimal balance;

    private User user;

    public Account() {
        this.setId(AppUtil.getNextAccountNumber());
        this.status = Status.ACTIVE;
        this.balance = new BigDecimal(0);
        this.user = new User();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void debitAmount(BigDecimal amount) {
        int val = this.getBalance().compareTo(amount);
        if (val < 0)
            throw new InsufficientBalanceException("Insufficient Balance " + amount.intValue());
        this.balance = this.balance.subtract(amount);
    }

    public void creditAmount(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    @Override
    public String toString() {
        return "Account{" +
                "status=" + status +
                ", balance=" + balance +
                '}';
    }
}
