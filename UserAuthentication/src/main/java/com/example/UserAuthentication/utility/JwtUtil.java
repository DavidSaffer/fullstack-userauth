package com.example.UserAuthentication.utility;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for handling JWT (JSON Web Token) operations, including creation, validation, and extraction of claims.
 */
@Component
public class JwtUtil {
    private final String secretKey = "secret";  // Use a complex secret key in production

    @Value("${jwt.expiration.hours}")
    private long expirationTimeHours;
    private long expirationTime;  // 3600000 = 1 hour in milliseconds


    @PostConstruct
    public void init() {
        expirationTime = 3600000 * expirationTimeHours;  // Initialize after properties are loaded
    }
    /**
     * Generates a JWT for a specified username.
     *
     * @param username the username for which the token is being created
     * @return the generated JWT string or null if an error occurs during token creation
     */
    public String generateToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String token = JWT.create()
                    .withSubject(username)
                    .withIssuer("auth0")
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Extracts the username from the JWT (DOES NOT VALIDATE).
     *
     * @param token the JWT from which to extract the username
     * @return the username extracted from the token
     */
    public String getUsernameFromToken(String token) {
        // Decoding without verifying
        String username = JWT.decode(token).getSubject();
        return username;
    }

    /**
     * Checks if a JWT has expired (DOES NOT VALIDATE).
     *
     * @param token the JWT to check for expiration
     * @return true if the token has expired, otherwise false
     */
    public boolean isTokenExpired(String token) {
        // Decoding without verifying
        Date expiration = JWT.decode(token).getExpiresAt();
        return expiration.before(new Date());
    }

    /**
     * Validates a JWT's integrity and checks the validity of its claims.
     *
     * @param token the JWT to validate
     * @return true if the token is valid, otherwise false
     */
    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            return false;
        }
    }
}

