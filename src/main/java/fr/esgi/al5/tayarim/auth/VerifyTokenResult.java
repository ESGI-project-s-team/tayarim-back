package fr.esgi.al5.tayarim.auth;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Classe permettant la transmission des données venant du token.
 */
@Data
public class VerifyTokenResult {

  @NonNull
  private Long id;

  @NonNull
  private Boolean isAdmin;

  @NonNull
  private Boolean isSuperAdmin;

  /**
   * Constructeur pour la classe VerifyTokenResult.
   *
   * @param id           Id inscrit dans le token
   * @param isAdmin      Indicateur du role admin inscrit dans le token
   * @param isSuperAdmin Indicateur du role SuperAdmin inscrit dans le token
   */
  @Builder
  public VerifyTokenResult(@NonNull Long id, @NonNull Boolean isAdmin,
      @NonNull Boolean isSuperAdmin) {
    this.id = id;
    this.isAdmin = isAdmin;
    this.isSuperAdmin = isSuperAdmin;
  }
}
