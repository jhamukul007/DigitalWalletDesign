package com.dwp;

import com.dwp.dtos.PaymentRequestDto;
import com.dwp.models.ConsoleLogging;
import com.dwp.models.Logging;
import com.dwp.services.tx.TransactionService;
import com.dwp.services.tx.account.AccountService;
import com.dwp.services.tx.account.AccountServiceImpl;
import com.dwp.services.tx.impl.TransactionServiceImpl;

import java.util.Scanner;

public class MainCommandRunner {
    public static void main(String[] args) {
        Logging logger = new ConsoleLogging();
        AccountService accountService = new AccountServiceImpl(logger);
        TransactionService transactionService = new TransactionServiceImpl(logger, accountService);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n OPTIONS");
            System.out.println("1. Create Wallet");
            System.out.println("2. Transfer Fund");
            System.out.println("3. Account Balance");
            System.out.println("4. Overview");
            System.out.println("5. Add Fund");
            System.out.println("6. Exit");
            System.out.print("Please enter options: ");
            int inputCommand = scanner.nextInt();
            try {
                switch (inputCommand) {
                    case 1: {
                        accountService.createAccount();
                        break;
                    }
                    case 2: {
                        PaymentRequestDto dto = new PaymentRequestDto();
                        System.out.print("From accountNumber: ");
                        dto.setFromAccount(scanner.nextLong());
                        System.out.print("To accountNumber: ");
                        dto.setToAccount(scanner.nextLong());
                        System.out.print("Amount: ");
                        dto.setAmount(scanner.nextBigDecimal());
                        transactionService.pay(dto);
                        break;
                    }

                    case 3: {
                        System.out.print("Enter account number : ");
                        accountService.getAccountBalance(scanner.nextLong());
                        break;
                    }
                    case 4: {
                        accountService.getAllAccountBalance();
                        break;
                    }

                    case 5: {
                        PaymentRequestDto dto = new PaymentRequestDto();
                        System.out.print("To accountNumber: ");
                        Long accountNumber = scanner.nextLong();
                        dto.setFromAccount(accountNumber);
                        dto.setToAccount(accountNumber);
                        System.out.print("Amount: ");
                        dto.setAmount(scanner.nextBigDecimal());
                        transactionService.addFund(dto);
                        break;
                    }
                    default:
                        System.exit(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
