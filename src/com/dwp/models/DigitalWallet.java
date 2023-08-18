package com.dwp.models;

import com.dwp.enums.Status;
import com.dwp.exceptions.InsufficientBalanceException;

import java.math.BigDecimal;

public class DigitalWallet extends BaseEntity {
    private Status status;
    private BigDecimal balance;

    public DigitalWallet() {
        this.status = Status.ACTIVE;
        this.balance = new BigDecimal(0);
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
        this.balance.subtract(amount);
    }

    public void creditAmount(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    @Override
    public String toString() {
        return "DigitalWallet{" +
                "status=" + status +
                ", balance=" + balance +
                '}';
    }
}
