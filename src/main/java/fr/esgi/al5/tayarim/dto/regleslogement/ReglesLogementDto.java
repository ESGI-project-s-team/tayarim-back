package fr.esgi.al5.tayarim.dto.regleslogement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Classe pour la gestion des Dto de regles logement.
 */
@Data
@AllArgsConstructor
public class ReglesLogementDto {

  @NonNull
  private Integer id;

  @NonNull
  private String nom;

  @NonNull
  private String icone;

}
