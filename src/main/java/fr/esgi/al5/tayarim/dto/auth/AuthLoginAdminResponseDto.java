package fr.esgi.al5.tayarim.dto.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Objet de transfert de données (DTO) pour la réponse après une tentative de connexion réussie. Ce
 * DTO encapsule les informations essentielles de l'utilisateur authentifié, telles que son
 * identifiant, son token d'accès et son statut d'administrateur, et les info de l'utilisateur. Les
 * annotations Lombok génèrent automatiquement les méthodes getteurs, setteurs, toString, equals et
 * hashCode.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthLoginAdminResponseDto extends AuthLoginResponseDto {

  @NonNull
  private Boolean isSuperAdmin;

  @Builder
  public AuthLoginAdminResponseDto(@NonNull Long id, @NonNull String token, @NonNull Boolean admin,
      @NonNull String nom, @NonNull String prenom, @NonNull String email, @NonNull String numTel,
      @NonNull Boolean isSuperAdmin) {
    super(id, token, admin, nom, prenom, email, numTel);
    this.isSuperAdmin = isSuperAdmin;
  }

  // No need for explicit getters, setters, or constructors
}
