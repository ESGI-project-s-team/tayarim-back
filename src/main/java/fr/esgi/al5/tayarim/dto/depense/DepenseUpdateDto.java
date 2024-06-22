package fr.esgi.al5.tayarim.dto.depense;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Classe pour la gestion des Dto de création de dépense.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class DepenseUpdateDto {

  @Valid

  @Size(min = 1, max = 200, message = "error_expense_invalid_libelle")
  private String libelle;

  @Min(value = 1, message = "error_expense_invalid_prix")
  private Float prix;

}
