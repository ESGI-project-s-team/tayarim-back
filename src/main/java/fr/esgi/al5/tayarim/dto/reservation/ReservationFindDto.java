package fr.esgi.al5.tayarim.dto.reservation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Classe pour la gestion des Dto de création de réservation.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class ReservationFindDto {

  @Valid

  @NotBlank(message = "error_reservation_invalid_code")
  @Pattern(regexp = "RESA-[0-9]{6}", message = "error_reservation_invalid_code")
  private String code;

  @NotBlank(message = "error_reservation_invalid_identifier")
  @Pattern(regexp = "([\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4})"
      + "|(^[+]?[(]?\\d{3}[)]?[-\\s.]?\\d{3}[-\\s.]?\\d{4,6}$)",
      message = "error_reservation_invalid_identifier"
  )
  private String identifier;

  @NotBlank(message = "error_reservation_invalid_arrival")
  @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])",
      message = "error_reservation_invalid_arrival")
  private String dateArrivee;

}
