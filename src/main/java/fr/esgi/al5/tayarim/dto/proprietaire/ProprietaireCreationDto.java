package fr.esgi.al5.tayarim.dto.proprietaire;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO pour la création de nouveaux propriétaires dans le système. Contient des champs avec des
 * validations pour assurer l'intégrité des données lors de l'enregistrement.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class ProprietaireCreationDto {

  @Valid

  @NotBlank(message = "error_owner_invalid_name")
  @Size(min = 1, max = 20, message = "error_owner_invalid_name")
  @Pattern(regexp = "[a-zA-Z -]*", message = "error_owner_invalid_name")
  private String nom;

  @NotBlank(message = "error_owner_invalid_firstName")
  @Pattern(regexp = "[a-zA-Z -]*", message = "error_owner_invalid_firstName")
  @Size(min = 1, max = 20, message = "error_owner_invalid_firstName")
  private String prenom;

  @NotBlank(message = "error_owner_invalid_mail")
  @Pattern(regexp = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "error_owner_invalid_mail")
  //@Email(message = "error_owner_invalid_mail")
  private String email;

  @NotBlank(message = "error_owner_invalid_phone")
  @Pattern(regexp = "^[+]?[(]?\\d{3}[)]?[-\\s.]?\\d{3}[-\\s.]?\\d{4,6}$",
      message = "error_owner_invalid_phone")
  private String numTel;

  @NotBlank(message = "error_owner_invalid_address")
  @Size(min = 1, max = 300, message = "error_owner_invalid_address")
  private String adresse;

  @NotBlank(message = "error_owner_invalid_lang")
  @Size(min = 2, max = 2, message = "error_owner_invalid_lang")
  @Pattern(regexp = "fr|en", message = "error_owner_invalid_lang")
  private String lang;

  // No need for explicit getters, setters, or constructors
}
