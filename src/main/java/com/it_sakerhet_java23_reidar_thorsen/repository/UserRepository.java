package com.it_sakerhet_java23_reidar_thorsen.repository;

import com.it_sakerhet_java23_reidar_thorsen.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}

