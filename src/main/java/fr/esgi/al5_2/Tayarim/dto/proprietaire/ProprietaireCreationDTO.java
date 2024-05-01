package fr.esgi.al5_2.Tayarim.dto.proprietaire;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data // Generates getters, setters, toString, equals, and hashCode methods
@RequiredArgsConstructor
@AllArgsConstructor // Generates an all-args constructor
@Builder
public class ProprietaireCreationDTO {

  @Valid

  @NotBlank(message = "error_owner_invalid_name")
  private String nom;

  @NotBlank(message = "error_owner_invalid_firstName")
  private String prenom;

  @NotBlank(message = "error_owner_invalid_mail")
  @Email(message = "error_owner_invalid_mail")
  private String email;

  @NotBlank(message = "error_owner_invalid_phone")
  @Pattern(regexp = "\\+?[\\d\\s\\-.()]+", message = "error_owner_invalid_phone")
  private String numTel;

  // No need for explicit getters, setters, or constructors
}
