package fr.esgi.al5.tayarim.dto.indisponibilite;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Classe pour la gestion des Dto de création d'Indisponibilite'.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class IndisponibiliteCreationDto {

  @Valid

  @NotBlank(message = "error_indisponibilite_invalid_start_date")
  @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])",
      message = "error_indisponibilite_invalid_start_date")
  private String dateDebut;

  @NotBlank(message = "error_indisponibilite_invalid_end_date")
  @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])",
      message = "error_indisponibilite_invalid_end_date")
  private String dateFin;

  @NotNull(message = "error_indisponibilite_invalid_home")
  @Min(value = 1, message = "error_indisponibilite_invalid_home")
  private Long idLogement;


}
