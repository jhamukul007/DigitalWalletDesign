package com.dwp.models;

public class ConsoleLogging implements Logging{

    @Override
    public void log(Object input) {
        System.out.println(input);
        System.out.println("--------------------------");
    }
}
