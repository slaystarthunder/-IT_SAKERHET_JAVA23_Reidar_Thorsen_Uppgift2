package com.it_sakerhet_java23_reidar_thorsen.service;

import com.it_sakerhet_java23_reidar_thorsen.model.TimeCapsuleEntity;
import com.it_sakerhet_java23_reidar_thorsen.model.UserEntity;
import com.it_sakerhet_java23_reidar_thorsen.repository.TimeCapsuleRepository;
import com.it_sakerhet_java23_reidar_thorsen.utils.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;

@Service
public class TimeCapsuleService {

    @Autowired
    private TimeCapsuleRepository timeCapsuleRepository;

    public void createTimeCapsule(String message, UserEntity user, SecretKey key) throws Exception {
        // Encrypt the message using the provided key
        String encryptedMessage = AESUtil.encrypt(message, key);

        // Convert the key to a string for storage
        String encryptedKey = AESUtil.keyToString(key);

        // Save the time capsule with the encrypted message and key
        TimeCapsuleEntity timeCapsule = new TimeCapsuleEntity(encryptedMessage, user, encryptedKey);
        timeCapsuleRepository.save(timeCapsule);
    }

    public String decryptTimeCapsule(TimeCapsuleEntity timeCapsule, SecretKey key) throws Exception {
        // Decrypt the message using the provided key
        return AESUtil.decrypt(timeCapsule.getEncryptedMessage(), key);
    }

    public List<TimeCapsuleEntity> getTimeCapsulesForUser(UserEntity user) {
        return timeCapsuleRepository.findByUser(user);
    }
}
