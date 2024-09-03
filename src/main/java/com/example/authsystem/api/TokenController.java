package com.example.authsystem.api;

import com.example.authsystem.auth.TokenManager;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class TokenController {

    @Autowired
    private TokenManager tokenManager;

    @GetMapping("/token-info")
    public ResponseEntity<?> getTokenInfo(@RequestHeader("Authorization") String token) {
        String cleanedToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        if (tokenManager.tokenValidate(cleanedToken)) {
            Claims claims = tokenManager.getClaimsFromToken(cleanedToken);

            return ResponseEntity.ok(new TokenInfoResponse(
                    claims.getSubject(),
                    claims.get("mail", String.class),
                    claims.get("role", String.class)
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }
    }

    public static class TokenInfoResponse {
        private String username;
        private String email;
        private String role;

        public TokenInfoResponse(String username, String email, String role) {
            this.username = username;
            this.email = email;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
