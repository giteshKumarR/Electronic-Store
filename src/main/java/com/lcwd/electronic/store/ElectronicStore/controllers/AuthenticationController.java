package com.lcwd.electronic.store.ElectronicStore.controllers;

import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.payload.jwtPayload.JwtRequest;
import com.lcwd.electronic.store.ElectronicStore.payload.jwtPayload.JwtResponse;
import com.lcwd.electronic.store.ElectronicStore.security.JwtHelper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth-api")
public class AuthenticationController {

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper mapper;

    // Method to generate token
    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        logger.info("Email : {}, Password : {}", request.getEmail(), request.getPassword());

        // Before generating the token we will validate that the credentials are there in DB
        // for the user...
        this.doAuthenticate(request.getEmail(), request.getPassword());

        // Now we will get the user details after authentication
        User user = (User) userDetailsService.loadUserByUsername(request.getEmail());

        // Generate the Token for the user
        String token = jwtHelper.generateToken(user);
        logger.info("Token : {}", token);

        JwtResponse response = JwtResponse.builder()
                .token(token)
                .user(mapper.map(user, UserDto.class))
                .build();

        return ResponseEntity.ok(response);
    }

    private void doAuthenticate(String email, String password) {
        try{
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authentication);
        }catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid Username and Password !!");
        }
    }
}
