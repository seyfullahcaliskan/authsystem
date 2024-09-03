package com.example.authsystem;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password1";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Şifrelenmiş Şifre: " + encodedPassword);
    }
}