package com.lcwd.electronic.store.ElectronicStore.services;

import com.lcwd.electronic.store.ElectronicStore.dtos.RefreshTokenDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;

public interface RefreshTokenService {
    // Create Refresh Token
    RefreshTokenDto createRefreshToken(String username);

    // Find the refresh token
    RefreshTokenDto findByToken(String token);

    // Verify the expiry of the refresh token
    RefreshTokenDto verifyRefreshToken(RefreshTokenDto token);

    // Get the user details from the refresh token
    UserDto getUser(RefreshTokenDto refreshTokenDto);
}
