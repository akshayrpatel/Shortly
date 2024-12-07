package com.url.shortener.security;

import com.url.shortener.service.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtTokenExpiration;

    /**
     * Extract token string to validate from `Authorization` header
     * JWT token format "Authorization: Bearer <token>"
     */
    public static String getJwtFromHeader(HttpServletRequest request) {
        final String authHeader = "Authorization";
        final String bearerToken = request.getHeader(authHeader);
        if (bearerToken != null) {
            // we can use index 7 as we know token is preceded with "Bearer "
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Generate key to sign the JWT token
     */
    private Key getJwtSignatureKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Generate JWT token for the given user
     */
    public String generateToken(UserDetailsImpl userDetails) {
        final String userAuthorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date issuedAt = new Date();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userAuthorities)
                .issuedAt(issuedAt)
                .expiration(new Date(issuedAt.getTime() + jwtTokenExpiration))
                .signWith(getJwtSignatureKey())
                .compact();
    }

    /**
     * Extract username from previously server-issued JWT token
     */
    public String getUserNameFromToken(String authToken) {
        return Jwts.parser()
                .verifyWith((SecretKey) getJwtSignatureKey())
                .build()
                .parseSignedClaims(authToken)
                .getPayload()
                .getSubject();
    }

    /**
     * Validate the JWT token
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getJwtSignatureKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
