package fr.esgi.al5.tayarim.dto.depense;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Objet de Transfert de Données (DTO) représentant une Depense. Ce DTO est utilisé
 * pour encapsuler les informations essentielles d'une Depense.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor
public class DepenseDto {

  @Valid

  @NotBlank(message = "error_depense_invalid_libelle")
  @Size(min = 1, max = 200, message = "error_depense_invalid_libelle")
  private String libelle;

  @Min(value = 1, message = "error_depense_invalid_prix")
  private Float prix;

  @NotBlank(message = "error_depense_invalid_date")
  @Size(min = 1, max = 10, message = "error_depense_invalid_date")
  private String date;

  @Min(value = 1, message = "error_depense_invalid_idLogement")
  private Long idLogement;
}
