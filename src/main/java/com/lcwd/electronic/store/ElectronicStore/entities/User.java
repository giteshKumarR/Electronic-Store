package com.lcwd.electronic.store.ElectronicStore.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id // here we will not use any generation type as we will generate the
    // Ids ourselves...
    private String userId;

    @Column(name = "is_user_active", nullable = false)
    private boolean isUserActive;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email", unique = true)
    private String userEmail;

    @Column(name = "user_password", length=100)
    private String userPassword;

    @Column(name = "user_gender", length = 1)
    private String userGender;

    @Column(name = "user_about", length = 1000)
    private String userAbout;

    @Column(name = "user_profile_image")
    private String userProfileImage;

    @Column(name = "user_created_on")
    private LocalDateTime createdOn;

    @Column(name = "user_updated_on")
    private LocalDateTime updatedOn;

    @PrePersist
    public void onPrePersist() {
        // This will run only when the entity will be first created
        this.createdOn = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedOn = LocalDateTime.now();
    }
}