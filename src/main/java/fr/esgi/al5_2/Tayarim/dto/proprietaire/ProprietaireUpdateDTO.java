package fr.esgi.al5_2.Tayarim.dto.proprietaire;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import fr.esgi.al5_2.Tayarim.dto.logement.LogementDTO;

/**
 * DTO pour la mise à jour des informations d'un propriétaire. Permet de modifier les détails d'un
 * propriétaire existant dans la base de données.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class ProprietaireUpdateDTO {

  private String nom;
  private String prenom;
  private String email;
  private String numTel;
  private String motDePasse;

  // No need for explicit getters, setters, or constructors
}