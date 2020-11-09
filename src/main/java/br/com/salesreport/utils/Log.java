package br.com.salesreport.utils;

public class Log {

    public static void writeLog(String message, Object... values) {
        String formatted = message.formatted(values);
        System.out.println(formatted);
    }
}
