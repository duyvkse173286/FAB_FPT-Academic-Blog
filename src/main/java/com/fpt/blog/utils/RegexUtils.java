package com.fpt.blog.utils;

public class RegexUtils {

    private static final String PHONE_NUMBER_PATTER = "^[0|84|+84][35789]\\d{8}$";

    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        return phone.matches(PHONE_NUMBER_PATTER);
    }
}
