package fr.esgi.al5_2.Tayarim.dto.logement;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Data Transfer Object (DTO) representing a housing unit within the system. This class encapsulates
 * the details necessary for identifying a housing unit and its owner. It uses Lombok annotations to
 * automatically generate constructors, getters, setters, and other methods.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
public class LogementDTO {

  @NonNull
  private Long id;
  @NonNull
  private Long idProprietaire;


}
