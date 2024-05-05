package fr.esgi.al5_2.Tayarim.dto.logement;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Objet de Transfert de Données (DTO) représentant un logement dans le système. Ce DTO est utilisé
 * pour encapsuler les informations essentielles d'un logement, y compris son identifiant et
 * l'identifiant de son propriétaire, facilitant ainsi les opérations de gestion des logements.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
public class LogementDTO {

  @NonNull
  private Long id;
  @NonNull
  private Long idProprietaire;


}
