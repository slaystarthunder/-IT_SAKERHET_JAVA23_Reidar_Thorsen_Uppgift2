package com.it_sakerhet_java23_reidar_thorsen.controller;

import com.it_sakerhet_java23_reidar_thorsen.model.TimeCapsuleEntity;
import com.it_sakerhet_java23_reidar_thorsen.model.UserEntity;
import com.it_sakerhet_java23_reidar_thorsen.repository.UserRepository;
import com.it_sakerhet_java23_reidar_thorsen.service.TimeCapsuleService;
import com.it_sakerhet_java23_reidar_thorsen.utils.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TimeCapsuleController {

    @Autowired
    private TimeCapsuleService timeCapsuleService;

    @Autowired
    private UserRepository userRepository;

    // Create a time capsule
    @PostMapping("/timecapsules")
    public ResponseEntity<String> createTimeCapsule(@RequestBody String message, @AuthenticationPrincipal User user) throws Exception {
        UserEntity userEntity = userRepository.findByUsername(user.getUsername());

        // Generate a new AES key for this time capsule
        SecretKey secretKey = AESUtil.generateKey();

        // Create and store the time capsule with the encrypted message and secret key
        timeCapsuleService.createTimeCapsule(message, userEntity, secretKey);
        return ResponseEntity.ok("Time capsule created successfully.");
    }

    // Get time capsules for the logged-in user
    @GetMapping("/timecapsules")
    public List<String> getTimeCapsules(@AuthenticationPrincipal User user) throws Exception {
        UserEntity userEntity = userRepository.findByUsername(user.getUsername());
        List<TimeCapsuleEntity> timeCapsules = timeCapsuleService.getTimeCapsulesForUser(userEntity);

        return timeCapsules.stream()
                .map(timeCapsule -> {
                    try {
                        // Decrypt the message using the stored AES key
                        SecretKey secretKey = AESUtil.stringToKey(timeCapsule.getEncryptedKey());
                        return timeCapsuleService.decryptTimeCapsule(timeCapsule, secretKey);
                    } catch (Exception e) {
                        return "Error decrypting message";
                    }
                }).collect(Collectors.toList());
    }
}
