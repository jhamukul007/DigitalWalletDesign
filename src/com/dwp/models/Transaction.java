package com.dwp.models;

import com.dwp.enums.TransactionStatus;
import com.dwp.enums.TransactionType;
import com.dwp.utils.AppUtil;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction extends BaseEntity {
    private Long fromAccount;
    private Long toAccount;
    private BigDecimal amount;
    private TransactionStatus transactionStatus;

    private TransactionType transactionType;

    private Date txDate;

    private Transaction(Builder builder) {
        this.fromAccount = builder.fromAccount;
        this.toAccount = builder.toAccount;
        this.amount = builder.amount;
        this.transactionStatus = builder.transactionStatus;
        this.transactionType = builder.transactionType;
        this.txDate = builder.txDate == null ? new Date() : builder.txDate;
        this.setId(builder.id);
    }

    public static Transaction.Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long fromAccount;
        private Long toAccount;
        private BigDecimal amount;
        private TransactionStatus transactionStatus;
        private TransactionType transactionType;
        private Date txDate;

        public Builder fromAccount(Long fromAccount) {
            this.fromAccount = fromAccount;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder toAccount(Long toAccount) {
            this.toAccount = toAccount;
            return this;
        }

        public Builder transactionStatus(TransactionStatus transactionStatus) {
            this.transactionStatus = transactionStatus;
            return this;
        }

        public Builder transactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder txDate(Date txDate) {
            this.txDate = txDate;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Date getTxDate() {
        return txDate;
    }

    public Long getFromAccount() {
        return fromAccount;
    }

    public Long getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    @Override
    public Transaction clone() {
        return builder()
                .fromAccount(this.fromAccount)
                .toAccount(this.toAccount)
                .transactionType(this.transactionType)
                .transactionStatus(this.transactionStatus)
                .amount(this.amount)
                .build();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "fromAccount=" + fromAccount +
                ", toAccount=" + toAccount +
                ", amount=" + amount +
                ", transactionStatus=" + transactionStatus +
                ", transactionType=" + transactionType +
                '}';
    }
}
