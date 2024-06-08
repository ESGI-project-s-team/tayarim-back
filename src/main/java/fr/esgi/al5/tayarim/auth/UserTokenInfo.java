package fr.esgi.al5.tayarim.auth;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Classe transmettant les informations de l'utilisateur récupérer grace au token.
 */
@Data
@RequiredArgsConstructor
public class UserTokenInfo {

  @NonNull
  private Long id;

  @NonNull
  private Boolean isAdmin;

  @NonNull
  private Boolean isPasswordUpdated;

}
