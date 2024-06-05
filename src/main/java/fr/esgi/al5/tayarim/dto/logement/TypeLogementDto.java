package fr.esgi.al5.tayarim.dto.logement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Objet de Transfert de Données (DTO) représentant un type de logement dans le système. Ce DTO est
 * utilisé pour encapsuler les informations essentielles d'un type de logement.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
public class TypeLogementDto {

  @NonNull
  private Long id;
  @NonNull
  private String nom;
  @NonNull
  private String icone;


}
