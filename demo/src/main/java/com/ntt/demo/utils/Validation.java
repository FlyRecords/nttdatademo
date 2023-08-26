package com.ntt.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static final String PASS_REGEX =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d{2})[A-Za-z\\d]*$";

    public static boolean isValidPass(String email) {
        Pattern pattern = Pattern.compile(PASS_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
