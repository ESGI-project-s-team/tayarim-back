package fr.esgi.al5.tayarim.dto.proprietaire;

import fr.esgi.al5.tayarim.dto.logement.LogementUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Objet de Transfert de Données (DTO) pour la création d'un administrateur dans le système. Ce DTO
 * contient les informations nécessaires à l'enregistrement d'un nouvel administrateur, avec des
 * validations pour assurer l'intégrité des données.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class ProprietaireValidateDto {

  @Valid

  @NotNull
  private ProprietaireUpdateDto proprietaire;

  @NotNull
  private LogementUpdateDto logement;

  // No need for explicit getters, setters, or constructors
}
