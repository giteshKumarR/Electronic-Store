package com.lcwd.electronic.store.ElectronicStore.payload.jwtPayload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRequest {
    private String refreshToken;
}
