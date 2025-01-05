package com.lcwd.electronic.store.ElectronicStore.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
// This is used to perform JWT operations

// 1. JWT Generation.
// etc..
@Component
public class JwtHelper {
    // 1. Generate the token
    //  => we need the secret key that will be used for Token generation
    //     and the time for which the JWT Token will be valid...

    // Validity time in Milliseconds..
    public static final long TOKEN_VALIDITY_TIME = 5*60*60*1000; // 5 hours

    // Secret Key;
    public static final String SECRET_KEY = generateSecretKey();

    // Retrieve Username from JWT TOKEN
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimReslover) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimReslover.apply(claims);
    }

    // For retrieving any information form token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        // Below method is used with earlier versions of JWT..
        //return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

        // Below method will be used for JWT library version 0.12.xx.
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getPayload();

        // LATEST WAY for 1.0.0 versions of JWT Library.  (AVOID this as it throws error : io.jsonwebtoken.security.SignatureException: JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.)

//        SignatureAlgorithm hs512 = SignatureAlgorithm.HS512;
//        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), hs512.getJcaName());
//        return Jwts.parser()
//                .verifyWith(secretKeySpec) // Latest
//                .build()
//                .parseClaimsJws(token)
//                .getPayload();
    }

    // Check if the JWT token is expired or not
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        // this will check ki :
        // 1. expiration date ---> ---> ---> Current date : (expiration date is before current date ? Yes, return true) Means TOKEN is EXPIRED.
        // 2. current date ---> ---> ---> Expirations date : means (expiration date is before current date ? No, return false) Means TOKEN is NOT EXPIRED.
        return expiration.before(new Date());
    }

    // Retrieve Expiration date form token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Generate TOKEN
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Below method will do the generation process for the token..
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // Validate the token
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Method used for generating JWT secret Key
    private static String generateSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[64];
        secureRandom.nextBytes(key);
        String jwtSecret = Base64.getEncoder().encodeToString(key);
        return jwtSecret;
    }
}
