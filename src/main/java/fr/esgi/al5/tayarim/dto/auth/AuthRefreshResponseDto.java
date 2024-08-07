package fr.esgi.al5.tayarim.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Objet de transfert de données (DTO) pour la réponse après une tentative de refresh token réussie.
 * Ce DTO encapsule les informations d'authentification de l'utilisateur authentifié, telles que son
 * identifiant, son token d'accès et son statut d'administrateur, et les infos de l'utilisateur. Les
 * annotations Lombok génèrent automatiquement les méthodes getteurs, setteurs, toString, equals et
 * hashCode.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor// Generates an all-args constructor
public class AuthRefreshResponseDto {

  @NonNull
  private Long id;

  @NonNull
  private Boolean admin;

  @NonNull
  private Boolean isPasswordUpdated;

  @NonNull
  private String accessToken;

  @NonNull
  private String refreshToken;

  @NonNull
  private String tokenType;

  // No need for explicit getters, setters, or constructors
}
