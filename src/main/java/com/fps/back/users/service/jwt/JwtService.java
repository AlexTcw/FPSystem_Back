package com.fps.back.users.service.jwt;

import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Map;

public interface JwtService {

    String getToken(UserDetails userDetails);
    String getToken(Map<String, Object> extraClaims, UserDetails userDetails);
    SecretKey getSecretKey();
    String getUserNameFromToken(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
}
