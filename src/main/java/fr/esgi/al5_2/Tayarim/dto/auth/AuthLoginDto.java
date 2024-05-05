package fr.esgi.al5_2.Tayarim.dto.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO (Data Transfer Object) pour les données de connexion utilisées lors de l'authentification
 * d'un utilisateur. Ce DTO capture l'adresse email et le mot de passe fournis par l'utilisateur
 * lors de la tentative de connexion.
 */
@Data
@AllArgsConstructor
public class AuthLoginDto {

  @Valid

  @NotBlank(message = "error_owner_invalid_mail")
  @Email(message = "error_owner_invalid_mail")
  private String email;

  @NotBlank(message = "error_owner_invalid_password")
  private String motDePasse;
}
