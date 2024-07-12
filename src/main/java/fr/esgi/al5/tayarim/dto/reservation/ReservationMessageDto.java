package fr.esgi.al5.tayarim.dto.reservation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class ReservationMessageDto {

  @Valid

  @NotBlank(message = "error_reservation_invalid_message")
  @Size(min = 1, max = 2000, message = "error_reservation_invalid_message")
  private String message;

}
