package fr.esgi.al5.tayarim.dto.logement;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO pour la recherche de logement dans le système.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class LogementSearchDto {

  @Valid

  @Size(min = 1, max = 50, message = "error_search_destination_invalid")
  private String destination;

  @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])",
      message = "error_search_invalid_date_arrival")
  private String dateArrivee;

  @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])",
      message = "error_search_invalid_date_departure")
  private String dateDepart;

  @Min(value = 1, message = "error_search_nbPersonnes_invalid")
  private Integer nbPersonnes;


}
