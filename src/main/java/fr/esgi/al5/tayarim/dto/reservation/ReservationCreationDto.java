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
public class ReservationCreationDto {

  @Valid

  @NotBlank(message = "error_reservation_invalid_mail")
  @Pattern(regexp = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "error_reservation_invalid_mail")
  private String email;

  @NotBlank(message = "error_reservation_invalid_phone")
  @Pattern(regexp = "^[+]?[(]?\\d{3}[)]?[-\\s.]?\\d{3}[-\\s.]?\\d{4,6}$",
      message = "error_reservation_invalid_phone")
  private String numTel;

  @NotBlank(message = "error_reservation_invalid_name")
  @Size(min = 1, max = 20, message = "error_reservation_invalid_name")
  @Pattern(regexp = "[a-zA-Z]*", message = "error_reservation_invalid_name")
  private String nom;

  @NotBlank(message = "error_reservation_invalid_firstName")
  @Pattern(regexp = "[a-zA-Z]*", message = "error_reservation_invalid_firstName")
  @Size(min = 1, max = 20, message = "error_reservation_invalid_firstName")
  private String prenom;

  @NotNull(message = "error_reservation_invalid_nbPersonnes")
  @Min(value = 1, message = "error_reservation_invalid_nbPersonnes")
  private Integer nbPersonnes;

  @Min(value = 1, message = "error_reservation_invalid_amount")
  private Float montant;

  @NotBlank(message = "error_reservation_invalid_arrival")
  @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])",
      message = "error_reservation_invalid_arrival")
  private String dateArrivee;

  @NotBlank(message = "error_reservation_invalid_departure")
  @Pattern(regexp = "[0-9]{4}-(0[1-9]|1[0-2])-([0-2][0-9]|3[0-1])",
      message = "error_reservation_invalid_departure")
  private String dateDepart;

  @NotNull(message = "error_reservation_invalid_home")
  @Min(value = 1, message = "error_reservation_invalid_home")
  private Long idLogement;

  @Size(min = 1, message = "error_reservation_invalid_payment_intent")
  private String paymentIntent;

}
