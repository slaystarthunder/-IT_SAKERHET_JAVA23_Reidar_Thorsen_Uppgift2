package com.it_sakerhet_java23_reidar_thorsen.repository;

import com.it_sakerhet_java23_reidar_thorsen.model.TimeCapsuleEntity;
import com.it_sakerhet_java23_reidar_thorsen.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeCapsuleRepository extends JpaRepository<TimeCapsuleEntity, Long> {
    List<TimeCapsuleEntity> findByUser(UserEntity user);
}
