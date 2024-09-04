package com.example.authsystem.Services;

import com.example.authsystem.Entities.UserEntity;
import com.example.authsystem.Repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserEntity registerUser(UserEntity userEntity) {
        if (userRepository.findByUsername(userEntity.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(userEntity.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUsername(userEntity.getUsername());
        newUser.setEmail(userEntity.getEmail());
        newUser.setPassword(passwordEncoder.encode(userEntity.getPassword())); // Şifreyi hash'leyin
       newUser.setRole("ROLE_USER"); // Varsayılan rol

        return userRepository.save(newUser);
    }
}
