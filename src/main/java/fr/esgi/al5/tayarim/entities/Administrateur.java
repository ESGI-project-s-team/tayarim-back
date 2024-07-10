package fr.esgi.al5.tayarim.entities;

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
@Entity(name = "ADMINISTRATEUR")
@PrimaryKeyJoinColumn(name = "IDUSER")
public class Administrateur extends Utilisateur {

  @Builder
  public Administrateur(@NonNull String nom, @NonNull String prenom, @NonNull String email,
      @NonNull String numTel, @NonNull String motDePasse) {
    // Utiliser super() pour appeler le constructeur de la classe parente
    super(nom, prenom, email, numTel, motDePasse);
  }


}
