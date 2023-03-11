package com.chessai.utils;

public class BitboardUtil {
    private static final long universe = 0b1111111111111111111111111111111111111111111111111111111111111111L;

    // 10 | 01 = 11
    public static long union(long a, long b) {
        return a | b;
    }

    // 11 & 01 = 01
    public static long subset(long a, long b) {
        return a & b;
    }

    // 00 == 00
    public static boolean bitboardIsEmpty(long b) {
        return b == 0;
    }

    // 11 == 11
    public static boolean bitboardIsFull(long b) {
        return b == universe;
    }
}
