package fr.esgi.al5.tayarim.dto.amenagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Classe pour la gestion des Dto des amenagements.
 */
@Data
@AllArgsConstructor
public class AmenagementDto {

  @NonNull
  private Long id;

  @NonNull
  private String nom;

  @NonNull
  private String icone;

  @NonNull
  private Long idCategorieAmenagement;

}
