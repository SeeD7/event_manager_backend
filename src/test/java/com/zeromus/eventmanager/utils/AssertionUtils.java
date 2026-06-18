package com.zeromus.eventmanager.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertionUtils {
    private AssertionUtils() {}

    public static void assertExceptionMessageContains(Exception exception, String message) {
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(message));
    }
}
