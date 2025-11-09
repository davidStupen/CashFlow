package com.example.cash_flow_backend.security.service;

import com.example.cash_flow_backend.security.model.dto.UserTokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    public String generateToken(UserTokenDTO user){
        return Jwts.builder()
                .subject(user.username())
                .claim("role", user.role())
                .claim("userId", user.userId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1_000 * 60 * 15))
                .signWith(this.getKey())
                .compact();
    }
    private SecretKey getKey(){
        String secreteKey = "g".repeat(48);
        return Keys.hmacShaKeyFor(secreteKey.getBytes(StandardCharsets.UTF_8));
    }
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(this.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimsFunction){
        Claims claims = this.extractAllClaims(token);
        return claimsFunction.apply(claims);
    }
    public String extractUsername(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    public boolean validationToken(String token, UserDetails userDetails) {
        return this.extractUsername(token).equalsIgnoreCase(userDetails.getUsername()) &&
                this.extractClaim(token, Claims::getExpiration).after(new Date());
    }
}
