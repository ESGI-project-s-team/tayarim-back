package fr.esgi.al5.tayarim.dto.indisponibilite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Classe pour la gestion des Dto d'Indisponibilite'.
 */
@Data
@AllArgsConstructor
public class IndisponibiliteDto {

  @NonNull
  private Long id;

  @NonNull
  private String dateDebut;

  @NonNull
  private String dateFin;

  @NonNull
  private Long idLogement;


}
