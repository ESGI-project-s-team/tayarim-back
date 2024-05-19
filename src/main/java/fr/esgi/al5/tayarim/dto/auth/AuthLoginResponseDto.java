package fr.esgi.al5.tayarim.dto.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
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
@Data // Generates getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public abstract class AuthLoginResponseDto {

  @NonNull
  private Long id;

  @NonNull
  private String token;

  @NonNull
  private Boolean admin;

  @NonNull
  private String nom;

  @NonNull
  private String prenom;

  @NonNull
  private String email;

  @NonNull
  private String numTel;

  // No need for explicit getters, setters, or constructors
}
