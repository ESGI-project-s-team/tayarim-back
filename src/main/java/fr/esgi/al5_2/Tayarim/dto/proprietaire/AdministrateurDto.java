package fr.esgi.al5_2.Tayarim.dto.proprietaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Objet de Transfert de Données (DTO) pour les administrateurs du système. Ce DTO encapsule les
 * informations essentielles d'un administrateur pour les opérations de récupération.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class AdministrateurDto {

  @NonNull
  private Long id;
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
