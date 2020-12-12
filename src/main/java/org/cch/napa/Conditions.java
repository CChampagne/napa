package org.cch.napa;

public class Conditions {
    public static boolean isEmptyOrNull(String value) {
        return value == null || value.trim().length() == 0;
    }
}
