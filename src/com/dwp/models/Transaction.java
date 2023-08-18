package com.dwp.models;

import com.dwp.enums.TransactionStatus;
import com.dwp.enums.TransactionType;
import com.dwp.utils.AppUtil;

import java.math.BigDecimal;

public class Transaction extends BaseEntity {
    private Long fromUserId;
    private Long toUserId;
    private BigDecimal amount;
    private TransactionStatus transactionStatus;

    private TransactionType transactionType;

    private String tx_between;

    private Transaction(Builder builder) {
        this.fromUserId = builder.fromUserId;
        this.toUserId = builder.toUserId;
        this.amount = builder.amount;
        this.transactionStatus = builder.transactionStatus;
        this.transactionType = builder.transactionType;
        this.tx_between = AppUtil.getTxBetween(fromUserId, toUserId);
        this.setId(builder.id);
    }

    public static Transaction.Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long fromUserId;
        private Long toUserId;
        private BigDecimal amount;
        private TransactionStatus transactionStatus;
        private TransactionType transactionType;

        public Builder fromUserId(Long fromUserId) {
            this.fromUserId = fromUserId;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder toUserId(Long toUserId) {
            this.toUserId = toUserId;
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

        public Transaction build() {
            return new Transaction(this);
        }
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public BigDecimal getAmount() {
        return amount;
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

    @Override
    public Transaction clone() {
        return builder()
                .fromUserId(this.fromUserId)
                .toUserId(this.toUserId)
                .transactionType(this.transactionType)
                .transactionStatus(this.transactionStatus)
                .amount(this.amount)
                .build();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", amount=" + amount +
                ", transactionStatus=" + transactionStatus +
                ", transactionType=" + transactionType +
                ", tx_between='" + tx_between + '\'' +
                '}';
    }
}
