package com.revature.admin.TravelPlanner.security;

import com.revature.admin.TravelPlanner.models.Admin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    public String generateToken(Admin admin){

        //// Included user details in the token payload
        // for enhanced authentication context
        Map<String,Object> claims = new HashMap<>();
        claims.put("adminId",admin.getAdminId());
        claims.put("firstName",admin.getFirstName());
        claims.put("lastName",admin.getLastName());
        claims.put("email",admin.getEmail());
        claims.put("master",admin.isMaster());
        claims.put("createdAt",admin.getCreatedAt());

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(admin.getAdminId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+jwtExpirationInMs)) // 24 hours
                .and()
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();

    }

    // Extract username from JWT token
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Validate JWT token
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUserId(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Extract a specific claim from JWT token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from JWT token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from JWT token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
