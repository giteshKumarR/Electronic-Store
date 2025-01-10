package com.lcwd.electronic.store.ElectronicStore.payload.jwtPayload;

import com.lcwd.electronic.store.ElectronicStore.dtos.RefreshTokenDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.RefreshToken;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private UserDto user;
//    private String refreshToken;
    private RefreshTokenDto refreshToken;
}
