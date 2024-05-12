package fr.esgi.al5.tayarim.dto.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO (Data Transfer Object) pour les données de connexion utilisées lors du refresh du token d'un
 * utilisateur. Ce DTO capture le refreshToken de l'utilisateur lors d'une authentification avec un
 * accessToken expiré ou invalide.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthRefreshDto {

  @Valid

  @NotBlank(message = "error_jwt_invalid_refreshToken") //TODO add to dico
  private String refreshToken;
}
