package com.satispay.protocore.log;

public class ProtoLogger {
    public static boolean verbose = true;

    public static void info(String string) {
        System.out.printf("[INFO] %s message: %s%n", verbose ? getVerboseInfo() : "", string);
    }

    public static void error(String string) {
        System.err.printf("[ERROR] %s message: %s%n", verbose ? getVerboseInfo() : "", string);
    }

    private static String getVerboseInfo() {
        return String.format("%s file: %s line: %d", Thread.currentThread(), Thread.currentThread().getStackTrace()[3].getFileName(), Thread.currentThread().getStackTrace()[3].getLineNumber());
    }
}
