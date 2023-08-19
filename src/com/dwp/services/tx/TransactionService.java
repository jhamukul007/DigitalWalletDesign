package com.dwp.services.tx;

import com.dwp.dtos.PaymentRequestDto;
import com.dwp.models.TransactionDetail;

public interface TransactionService {
    void pay(PaymentRequestDto paymentDto);

    void addFund(PaymentRequestDto fundDetails);
}
