package fr.esgi.al5_2.Tayarim.auth;

import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.Instant;
import java.time.chrono.ChronoPeriod;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtHelper {

    @Value("${PRIVATE_KEY}")
    private String privateKey;

    public String generateToken(String email) {
        var now = Instant.now();
        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(60*60))) //60s * 60m = 1h
                .signWith(getSignKey())
                .compact();
    }

    public String extractEmail(String token) {
        return getTokenBody(token).getSubject();
    }

    public Boolean validateToken(String token, Proprietaire proprietaire) {
        final String email = extractEmail(token);
        return email.equals(proprietaire.getEmail()) && !isTokenExpired(token);
    }

    private Claims getTokenBody(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException | ExpiredJwtException e) { // Invalid signature or expired token
            return null;
        }
    }

    private boolean isTokenExpired(String token) {
        Claims claims = getTokenBody(token);
        return claims.getExpiration().before(new Date());
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(privateKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}