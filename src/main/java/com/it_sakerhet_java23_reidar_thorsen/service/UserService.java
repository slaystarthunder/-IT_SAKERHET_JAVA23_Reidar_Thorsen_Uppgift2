package com.it_sakerhet_java23_reidar_thorsen.service;

import com.it_sakerhet_java23_reidar_thorsen.model.UserEntity;
import com.it_sakerhet_java23_reidar_thorsen.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();

    }

    public UserEntity registerUser(String username, String password, String email) {

        String hashedPassword = passwordEncoder.encode(password);

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);

        return userRepository.save(user);

    }

}
