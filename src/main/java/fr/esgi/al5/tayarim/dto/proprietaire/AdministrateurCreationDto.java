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
 * Objet de Transfert de Données (DTO) pour la création d'un administrateur dans le système. Ce DTO
 * contient les informations nécessaires à l'enregistrement d'un nouvel administrateur, avec des
 * validations pour assurer l'intégrité des données.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class AdministrateurCreationDto {

  @Valid

  @NotBlank(message = "error_admin_invalid_name")
  @Size(min = 1, max = 20, message = "error_admin_invalid_name")
  @Pattern(regexp = "[a-zA-Z -]*", message = "error_admin_invalid_name")
  private String nom;

  @NotBlank(message = "error_admin_invalid_firstName")
  @Pattern(regexp = "[a-zA-Z -]*", message = "error_admin_invalid_firstName")
  @Size(min = 1, max = 20, message = "error_admin_invalid_firstName")
  private String prenom;

  @NotBlank(message = "error_owner_invalid_mail")
  @Pattern(regexp = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "error_admin_invalid_mail")
  //@Email(message = "error_admin_invalid_mail")
  private String email;

  @NotBlank(message = "error_admin_invalid_phone")
  @Pattern(regexp = "^[+]?[(]?\\d{3}[)]?[-\\s.]?\\d{3}[-\\s.]?\\d{4,6}$",
      message = "error_admin_invalid_phone")
  private String numTel;

  @NotBlank(message = "error_admin_invalid_password")
  @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?\\d)(?=.*?[!@#$%&*()_+\\-=\\[\\]?]).{8,}$",
      message = "error_admin_invalid_password")
  private String motDePasse;

  // No need for explicit getters, setters, or constructors
}
