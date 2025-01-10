package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;

import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenDto {
    private Integer refreshTokenId;
    private String token;
    private Instant expiryDate;
}
