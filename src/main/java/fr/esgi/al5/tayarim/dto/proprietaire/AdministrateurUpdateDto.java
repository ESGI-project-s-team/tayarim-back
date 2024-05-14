package fr.esgi.al5.tayarim.dto.proprietaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO pour la mise à jour des informations d'un administrateur. Utilisé pour passer les données
 * modifiées à l'API pour les opérations de mise à jour.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class AdministrateurUpdateDto {

  private String nom;
  private String prenom;
  private String email;
  private String numTel;
  private String motDePasse;

  // No need for explicit getters, setters, or constructors
}
