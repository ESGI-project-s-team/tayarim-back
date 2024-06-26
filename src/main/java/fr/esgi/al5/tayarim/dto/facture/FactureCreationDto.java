package fr.esgi.al5.tayarim.dto.facture;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Classe pour la gestion des Dto de création de facture.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class FactureCreationDto {

  @Valid
  @Min(value = 1, message = "error_invoice_invalid_month")
  @Max(value = 12, message = "error_invoice_invalid_month")
  @NotNull
  private Long month;

  @Min(value = 2000, message = "error_invoice_invalid_year")
  @Max(value = 3000, message = "error_invoice_invalid_year")
  private Long year;

  @Min(value = 1, message = "error_invoice_invalid_owner_id")
  @NotNull
  private Long idProprietaire;
}
