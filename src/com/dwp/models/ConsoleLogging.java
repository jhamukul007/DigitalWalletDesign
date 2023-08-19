package com.dwp.models;

public class ConsoleLogging implements Logging {

    @Override
    public void log(Object input) {
        System.out.println(input);
        System.out.println("--------------------------");
    }

    @Override
    public void info(String tag, Object input) {
        System.out.println(tag + " ::   " + input);
        System.out.println("************************************************");
    }
}
