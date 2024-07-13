package fr.esgi.al5.tayarim.dto.auth;

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
public class UserRecoverDto {

  @Valid

  @NotBlank(message = "error_user_invalid_mail")
  @Pattern(regexp = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "error_user_invalid_mail")
  private String email;

  // No need for explicit getters, setters, or constructors
}
