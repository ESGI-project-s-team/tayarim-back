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
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Composant utilitaire pour la gestion des JWT (JSON Web Tokens).
 */
@Component
public class JwtHelper {

  @Value("${PRIVATE_KEY}")
  private String privateKey;

  /**
   * Génère un token JWT pour un utilisateur.
   *
   * @param email   L'email de l'utilisateur.
   * @param uuid    L'UUID associé à l'utilisateur.
   * @param isAdmin Booléen indiquant si l'utilisateur est un administrateur.
   * @return Le token JWT généré.
   */
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

  /**
   * Extrait le sujet d'un token JWT.
   *
   * @param token Le token JWT.
   * @return Le sujet du token.
   * @throws TokenExpireOrInvalidException Si le token est expiré ou invalide.
   */
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

  /**
   * Extrait l'email de l'utilisateur à partir d'un token JWT.
   *
   * @param token Le token JWT.
   * @return L'email extrait du token.
   * @throws TokenExpireOrInvalidException Si le token est expiré ou invalide.
   */
  public String extractEmail(String token) {
    if (extractSubject(token).split(";")[0] == null) {
      throw new TokenExpireOrInvalidException();
    }

    return extractSubject(token).split(";")[0];
  }

  /**
   * Détermine si l'utilisateur associé au token est un administrateur.
   *
   * @param token Le token JWT.
   * @return Booléen indiquant si l'utilisateur est un administrateur.
   * @throws TokenExpireOrInvalidException Si le token est expiré ou invalide.
   */
  public String extractUuid(String token) {
    if (extractSubject(token).split(";")[1] == null) {
      throw new TokenExpireOrInvalidException();
    }

    return extractSubject(token).split(";")[1];
  }

  /**
   * Détermine si l'utilisateur associé au token est un administrateur.
   *
   * @param token Le token JWT.
   * @return Booléen indiquant si l'utilisateur est un administrateur.
   * @throws TokenExpireOrInvalidException Si le token est expiré ou invalide.
   */
  public Boolean extractAdmin(String token) {
    if (extractSubject(token).split(";")[2] == null) {
      throw new TokenExpireOrInvalidException();
    }

    return Boolean.parseBoolean(extractSubject(token).split(";")[2]);
  }

  /**
   * Valide un token JWT en vérifiant l'email et l'UUID.
   *
   * @param token Le token JWT à valider.
   * @param email L'email à vérifier.
   * @return Booléen indiquant si le token est valide.
   */
  public Boolean validateToken(String token, String email, String uuid) {
    String extractedEmail = extractEmail(token);
    String extractedUuid = extractUuid(token);

    return extractedEmail != null
        && extractedUuid != null
        && extractedEmail.equals(email)
        && extractedUuid.equals(uuid)
        && !isTokenExpired(token);
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