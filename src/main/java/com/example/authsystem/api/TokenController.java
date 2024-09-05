package com.example.authsystem.api;

import com.example.authsystem.auth.TokenManager;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
public class TokenController {

    private final TokenManager tokenManager;

    @Autowired
    public TokenController(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @GetMapping("/info")
    public ResponseEntity<TokenInfoResponse> getTokenInfo(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : tokenHeader;
        if (tokenManager.validateToken(token)) {
            Claims claims = tokenManager.getClaimsFromToken(token);
            TokenInfoResponse response = new TokenInfoResponse(
                    claims.getSubject(),
                    claims.get("email", String.class),
                    claims.get("role", String.class)
            );
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
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
