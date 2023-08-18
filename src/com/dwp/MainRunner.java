package com.dwp;

import com.dwp.dtos.PaymentRequestDto;
import com.dwp.models.ConsoleLogging;
import com.dwp.models.Logging;
import com.dwp.models.User;
import com.dwp.services.UserService;
import com.dwp.services.tx.TransactionService;
import com.dwp.services.tx.impl.TransactionServiceImpl;

import java.math.BigDecimal;

public class MainRunner {
    public static void main(String[] args) {

        Logging logger = new ConsoleLogging();
        UserService userService = new UserService(logger);
        TransactionService transactionService = new TransactionServiceImpl(userService, logger);

        User user1 = new User(1L, "Mukul", "muk@muk.jha");
        User user2 = new User(2L, "Jack", "jack@muk.com");
        User user3 = new User(3L, "Rahul", "rahul@muk.com");

        userService.registerUser(user1);
        userService.registerUser(user2);
        userService.registerUser(user3);

        logger.log(userService.getUserByIdOrThrowException(1L));
        logger.log(userService.getUserByIdOrThrowException(2L));
        logger.log(userService.getUserByIdOrThrowException(3L));

        transactionService.addFund(new PaymentRequestDto(1L, 1L, new BigDecimal(5000)));

        logger.log(userService.getUserByIdOrThrowException(1L));

        logger.log(transactionService.accountStatement(1L));

        transactionService.pay(new PaymentRequestDto(1L, 2L, new BigDecimal(2000)) );
        logger.log(transactionService.accountStatement(1L));
        logger.log(transactionService.accountStatement(2L));

        transactionService.addFund(new PaymentRequestDto(2L,2L, new BigDecimal(25000)));
        logger.log(transactionService.accountStatement(2L));

    }
}
