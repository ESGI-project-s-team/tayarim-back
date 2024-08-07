package fr.esgi.al5.tayarim.dto.proprietaire;

import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;




/**
 * DTO pour les informations détaillées d'un propriétaire, incluant ses logements associés. Utilisé
 * pour afficher les informations complètes d'un propriétaire dans l'interface utilisateur ou les
 * API.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class ProprietaireDto {

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

  @NonNull
  private String adresse;

  @NonNull
  private LocalDateTime dateInscription;

  @NonNull
  private Boolean isPasswordUpdated;

  private List<LogementDto> logements;

  @NonNull
  private Float commission;

  @NonNull
  private Boolean isValidated;

  @NonNull
  private String lang;

  // No need for explicit getters, setters, or constructors
}
