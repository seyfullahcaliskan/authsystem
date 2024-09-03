package com.example.authsystem.auth;

import com.example.authsystem.Entities.UserEntity;
import com.example.authsystem.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenManager {

    // Key statik ve final olarak tanımlanır, bu durumda anahtar sabit olmalı
    private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final int validity = 5 * 60 * 1000; // Token geçerlilik süresi 5 dakika

    @Autowired
    private UserRepository userRepository;

    public String generateToken(String username) {
        UserEntity user = userRepository.findByUsername(username);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .claim("mail", user.getEmail())
                .setIssuer("AuthSystemApp")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(secretKey) // Kullanılan imza anahtarı
                .compact();
    }

    public boolean tokenValidate(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return !isExpired(token);
        } catch (Exception e) {
            e.printStackTrace(); // Detaylı hata logu
            return false;
        }
    }

    public String getUserFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public Claims getClaimsFromToken(String token) {
        // Token'dan tüm claim'leri almak için JWT'yi doğrular
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace(); // Hata durumunda detaylı log
            return null; // Hata durumunda null döndürülür
        }
    }
}
