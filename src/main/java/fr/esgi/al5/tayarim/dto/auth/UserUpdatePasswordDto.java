package fr.esgi.al5.tayarim.dto.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class UserUpdatePasswordDto {

  @Valid

  @NotBlank(message = "error_user_invalid_token")
  private String token;

  @NotBlank(message = "error_user_invalid_password")
  @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?\\d)(?=.*?[!@#$%&*()_+\\-=\\[\\]?]).{8,}$",
      message = "error_user_invalid_password")
  private String motDePasse;

  // No need for explicit getters, setters, or constructors
}
