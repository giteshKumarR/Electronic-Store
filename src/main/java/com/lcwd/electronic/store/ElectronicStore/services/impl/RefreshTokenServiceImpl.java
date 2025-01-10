package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.dtos.RefreshTokenDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.RefreshToken;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.exceptions.general.ResourseNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.repositories.RefreshTokenRepository;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import com.lcwd.electronic.store.ElectronicStore.services.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;

    private ModelMapper mapper;

    public RefreshTokenServiceImpl() {
    }

    @Autowired
    public RefreshTokenServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.mapper = mapper;
    }

    // Here we are doing Constructor injection instead of autowiring..
    public RefreshTokenServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshTokenDto createRefreshToken(String username) {
        User user = userRepository.findByUserEmail(username).orElseThrow(() -> new ResourseNotFoundException("user not found with the given email!!"));

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(null);

        if(refreshToken == null) {
            // Means we didn't get the token for the user then create a new one..
            refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusSeconds(5*24*60*60)) // Means 5 days from now
                    .build();
            logger.info("Refresh token : {}", refreshToken);
        } else {
            // we have the refresh token for the associate, then just extend the expiry for the refresh token
            // and change the token value...
            // So jitni baar user login kareg utni baar refresh token value hum change kar denge..
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusSeconds(5*24*60*60));
            logger.info("Refresh token : {}", refreshToken);
        }

        // Save the token / token details in DB
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        logger.info("Token saved in DB : {}", savedToken);
        return mapper.map(savedToken, RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto findByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() -> new ResourseNotFoundException("Refresh token Not found in the DB !!"));
        return mapper.map(refreshToken, RefreshTokenDto.class);
    }

    @Override
    public RefreshTokenDto verifyRefreshToken(RefreshTokenDto token) {

        var refreshToken = mapper.map(token, RefreshToken.class);

        if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
            // Then it means the refresh token is expired
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token Expired !!");
        }

        // otherwise just return the initial token
        return token;
    }

    @Override
    public UserDto getUser(RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenDto.getToken()).orElseThrow(() -> new ResourseNotFoundException("Refresh token not found!!"));
        User user = refreshToken.getUser();
        return mapper.map(user, UserDto.class);
    }
}
