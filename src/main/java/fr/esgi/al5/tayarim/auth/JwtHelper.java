package fr.esgi.al5.tayarim.auth;

import fr.esgi.al5.tayarim.exceptions.TokenExpireOrInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
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
   * @param id      L'id de l'utilisateur.
   * @param uuid    L'UUID associé à l'utilisateur.
   * @param isAdmin Booléen indiquant si l'utilisateur est un administrateur.
   * @return Le token JWT généré.
   */
  public String generateToken(Long id, String uuid, boolean isAdmin, boolean isRefreshToken) {
    String subject = id.toString().concat(";").concat(uuid).concat(";")
        .concat(Boolean.toString(isAdmin));

    Instant now = Instant.now();
    Date expiration;
    if (isRefreshToken) {
      expiration = Date.from(now.plusSeconds(86400 * 7)); // 7j
    } else {
      expiration = Date.from(now.plusSeconds(86400)); //24h
    }
    return Jwts.builder()
        .subject(subject)
        .issuedAt(Date.from(now))
        .expiration(expiration) //24h
        .signWith(getSignKey())
        .compact();
  }

  /**
   * Génère un token JWT pour la récupération de mot de passe.
   *
   * @param email L'email de l'utilisateur.
   */
  public String generateRecoverToken(String email) {
    String subject = UUID.randomUUID().toString().concat(";").concat(email);

    Instant now = Instant.now();
    Date expiration;
    expiration = Date.from(now.plusSeconds(600)); //10min
    return Jwts.builder()
        .subject(subject)
        .issuedAt(Date.from(now))
        .expiration(expiration)
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
   * Extrait l'id de l'utilisateur à partir d'un token JWT.
   *
   * @param token Le token JWT.
   * @return L'id extrait du token.
   * @throws TokenExpireOrInvalidException Si le token est expiré ou invalide.
   */
  public Long extractId(String token) {
    if (extractSubject(token).split(";")[0] == null) {
      throw new TokenExpireOrInvalidException();
    }

    long id;

    try {
      id = Long.parseLong(extractSubject(token).split(";")[0]);
    } catch (Exception e) {
      throw new TokenExpireOrInvalidException();
    }

    return id;
  }

  /**
   * Extrait l'email de récupération à partir d'un token JWT.
   *
   * @param token Le token JWT.
   * @return L'email extrait du token.
   * @throws TokenExpireOrInvalidException Si le token est expiré ou invalide.
   */
  public String extractRecoveredEmail(String token) {
    if (extractSubject(token).split(";")[1] == null) {
      throw new TokenExpireOrInvalidException();
    }

    String email;

    try {
      email = extractSubject(token).split(";")[1];
    } catch (Exception e) {
      throw new TokenExpireOrInvalidException();
    }

    return email;
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
   * Valide un token JWT en vérifiant l'id' et l'UUID.
   *
   * @param token Le token JWT à valider.
   * @param id    L'id à vérifier.
   * @return Booléen indiquant si le token est valide.
   */
  public Boolean validateToken(String token, Long id, String uuid) {
    Long extractedId = extractId(token);
    String extractedUuid = extractUuid(token);

    return extractedId != null
        && extractedUuid != null
        && extractedId.equals(id)
        && extractedUuid.equals(uuid)
        && !isTokenExpired(token);
  }

  public Boolean validateRecoverToken(String token) {
    return !isTokenExpired(token);
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