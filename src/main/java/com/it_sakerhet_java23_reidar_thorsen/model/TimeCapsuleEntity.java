package com.it_sakerhet_java23_reidar_thorsen.model;

import jakarta.persistence.*;

@Entity
@Table(name = "time_capsules")
public class TimeCapsuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String encryptedMessage;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String encryptedKey; // Store the AES key as a base64-encoded string

    // Constructors
    public TimeCapsuleEntity() {}

    public TimeCapsuleEntity(String encryptedMessage, UserEntity user, String encryptedKey) {
        this.encryptedMessage = encryptedMessage;
        this.user = user;
        this.encryptedKey = encryptedKey;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getEncryptedKey() {
        return encryptedKey;
    }

    public void setEncryptedKey(String encryptedKey) {
        this.encryptedKey = encryptedKey;
    }
}
