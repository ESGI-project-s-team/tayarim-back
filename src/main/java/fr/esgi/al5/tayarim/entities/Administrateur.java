package fr.esgi.al5.tayarim.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Entité représentant un administrateur dans le système, héritant de la classe Utilisateur. Cette
 * classe utilise la stratégie de jointure pour la persistance et comprend des méthodes
 * constructeurs spécifiques pour faciliter la création d'instances avec des propriétés initiales
 * requises.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@PrimaryKeyJoinColumn(name = "IDUSER")
public class Administrateur extends Utilisateur {

  @Column(name = "ISSUPERADMIN", nullable = false)
  @NonNull
  private Boolean isSuperAdmin;

  /**
   * Builder pour l'entité Administrateur.
   *
   * @param nom Nom du proprietaire
   * @param prenom Prénom du proprietaire
   * @param email Email du proprietaire
   * @param numTel Numéro de téléphone du proprietaire
   * @param motDePasse Mot de passe du proprietaire
   * @param isSuperAdmin Indicateur du role SuperAdmin
   */
  @Builder
  public Administrateur(@NonNull String nom, @NonNull String prenom, @NonNull String email,
      @NonNull String numTel, @NonNull String motDePasse, @NonNull Boolean isSuperAdmin) {
    // Utiliser super() pour appeler le constructeur de la classe parente
    super(nom, prenom, email, numTel, motDePasse);
    this.isSuperAdmin = isSuperAdmin;
  }


}
