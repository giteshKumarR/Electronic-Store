package com.lcwd.electronic.store.ElectronicStore.payload.jwtPayload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtRequest {
    private String email;
    private String password;
}
