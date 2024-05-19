package fr.esgi.al5.tayarim.dto.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Objet de transfert de données (DTO) pour la réponse d'authentification. Ce DTO encapsule les
 * informations de réponse après une tentative de connexion, notamment l'identifiant de
 * l'utilisateur, son token et son statut administratif.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor// Generates an all-args constructor
public abstract class AuthResponseDto {

  @NonNull
  private Long id;

  @NonNull
  private String token;

  @NonNull
  private Boolean admin;

  // No need for explicit getters, setters, or constructors
}
