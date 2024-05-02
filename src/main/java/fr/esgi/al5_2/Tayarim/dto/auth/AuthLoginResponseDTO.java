package fr.esgi.al5_2.Tayarim.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Objet de transfert de données (DTO) pour la réponse après une tentative de connexion réussie. Ce
 * DTO encapsule les informations essentielles de l'utilisateur authentifié, telles que son
 * identifiant, son token d'accès et son statut d'administrateur. Les annotations Lombok génèrent
 * automatiquement les méthodes getteurs, setteurs, toString, equals et hashCode.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
public class AuthLoginResponseDTO {

  @NonNull
  private Long id;

  @NonNull
  private String token;

  private boolean admin;

  // No need for explicit getters, setters, or constructors
}
