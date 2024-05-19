package fr.esgi.al5.tayarim.dto.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Objet de transfert de données (DTO) pour la réponse d'authentification. Ce DTO encapsule les
 * informations de réponse après une tentative de connexion, notamment l'identifiant de
 * l'utilisateur, son token et son statut administratif.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthProprietaireResponseDto extends AuthResponseDto {

  @NonNull
  private Boolean isPasswordUpdated;

  @Builder
  public AuthProprietaireResponseDto(@NonNull Long id, @NonNull String token,
      @NonNull Boolean admin,
      @NonNull Boolean isPasswordUpdated) {
    super(id, token, admin);
    this.isPasswordUpdated = isPasswordUpdated;
  }
  // No need for explicit getters, setters, or constructors
}
