package com.ntt.demo.utils;

import com.ntt.demo.dto.PhoneDTO;
import com.ntt.demo.dto.UsuarioDTO;

import java.util.List;
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
    public static boolean isEmptyUsuario (UsuarioDTO usuarioDTO){
        return usuarioDTO.getName() == null && usuarioDTO.getEmail() == null && usuarioDTO.getPassword() == null
                && (usuarioDTO.getPhones() == null || usuarioDTO.getPhones().isEmpty());
    }
    public static boolean isEmptyPhones (List<PhoneDTO> phoneDTO){
        return phoneDTO == null || phoneDTO.isEmpty();
    }
}
