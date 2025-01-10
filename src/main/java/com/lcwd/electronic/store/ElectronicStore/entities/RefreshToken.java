package com.lcwd.electronic.store.ElectronicStore.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer refreshTokenId;
    private String token;
    private Instant expiryDate;

    // We are doing single direction mapping from Refresh Token side
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private User user;

    // these two fields fare for tracking purposes
    private LocalDateTime createdOn;
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
