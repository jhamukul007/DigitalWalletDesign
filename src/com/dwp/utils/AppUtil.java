package com.dwp.utils;

import java.util.Random;
import java.util.function.BiFunction;

public class AppUtil {
    private static Random random = new Random();
    private static Long accountNumbers = 1L;
    public static BiFunction<String, String, String> RESOURCE_NOT_FOUND = (input1, input2) -> String.format("%s not found on %s ", input1, input2);
    public static BiFunction<String, String, String> RESOURCE_ALREADY_FOUND = (input1, input2) -> String.format("%s already found on %s ", input1, input2);

    public static String getTxBetween(Long fromUserId, Long toUserId) {
        return fromUserId <= toUserId ? fromUserId + "_" + toUserId : toUserId + "_" + fromUserId;
    }

    public static Long getRandomId() {
        Long id = random.nextLong();
        return id % 1000;
    }

    public static Long getNextAccountNumber() {
        return accountNumbers++;
    }

}
