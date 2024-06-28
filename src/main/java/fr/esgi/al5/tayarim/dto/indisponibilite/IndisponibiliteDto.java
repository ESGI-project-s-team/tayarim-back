package fr.esgi.al5.tayarim.dto.indisponibilite;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
