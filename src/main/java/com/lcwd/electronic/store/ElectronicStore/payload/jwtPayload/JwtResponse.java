package com.lcwd.electronic.store.ElectronicStore.payload.jwtPayload;

import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private UserDto user;
    private String refreshToken;
}
