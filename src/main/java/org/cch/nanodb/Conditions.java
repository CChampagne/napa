package org.cch.nanodb;

public class Conditions {
    public static boolean isEmptyOrNull(String value) {
        return value == null || value.trim().length() == 0;
    }
}
