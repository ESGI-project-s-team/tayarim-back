package fr.esgi.al5_2.Tayarim.auth;

import fr.esgi.al5_2.Tayarim.exceptions.TokenExpireOrInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtHelper {

  @Value("${PRIVATE_KEY}")
  private String privateKey;

  public String generateToken(String email, String uuid, boolean isAdmin) {
    String subject = email.concat(";").concat(uuid).concat(";").concat(Boolean.toString(isAdmin));
    var now = Instant.now();
    return Jwts.builder()
        .subject(subject)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(86400))) //24h
        .signWith(getSignKey())
        .compact();
  }

  public String extractSubject(String token) {
    try {
      Claims tokenBody = getTokenBody(token);
      if (tokenBody == null) {
        throw new TokenExpireOrInvalidException();
      }

      return tokenBody.getSubject();
    } catch (Exception e) {
      throw new TokenExpireOrInvalidException();
    }
  }

  public String extractEmail(String token) {
    if (extractSubject(token).split(";")[0] == null) {
      throw new TokenExpireOrInvalidException();
    }

    return extractSubject(token).split(";")[0];
  }

  public String extractUuid(String token) {
    if (extractSubject(token).split(";")[1] == null) {
      throw new TokenExpireOrInvalidException();
    }

    return extractSubject(token).split(";")[1];
  }

  public Boolean extractAdmin(String token) {
    if (extractSubject(token).split(";")[2] == null) {
      throw new TokenExpireOrInvalidException();
    }

    return Boolean.parseBoolean(extractSubject(token).split(";")[2]);
  }

  public Boolean validateToken(String token, String email, String uuid) {
    String extractedEmail = extractEmail(token);
    String extractedUUID = extractUuid(token);

    return extractedEmail != null &&
        extractedUUID != null &&
        extractedEmail.equals(email) &&
        extractedUUID.equals(uuid) &&
        !isTokenExpired(token);
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
    if (claims == null) {
      return true;
    }
    return claims.getExpiration().before(new Date());
  }

  private SecretKey getSignKey() {
    byte[] keyBytes = Decoders.BASE64.decode(privateKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}