package com.dwp.dtos;

import java.math.BigDecimal;

public class PaymentRequestDto {
    private Long fromAccount;
    private Long toAccount;
    private BigDecimal amount;

    public PaymentRequestDto() {
    }

    public PaymentRequestDto(Long fromAccount, Long toAccount, BigDecimal amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public Long getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Long fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Long getToAccount() {
        return toAccount;
    }

    public void setToAccount(Long toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
