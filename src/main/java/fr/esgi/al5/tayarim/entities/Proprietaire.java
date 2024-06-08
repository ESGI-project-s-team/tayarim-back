package fr.esgi.al5.tayarim.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Entité représentant un propriétaire dans le système, étendant la classe Utilisateur. Un
 * propriétaire peut posséder plusieurs logements, gérés par une relation OneToMany. Cette classe
 * inclut des informations supplémentaires telles que la date d'inscription et un indicateur de mise
 * à jour du mot de passe.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@PrimaryKeyJoinColumn(name = "IDUSER")
public class Proprietaire extends Utilisateur {

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "proprietaire")
  private List<Logement> logements;

  @Column(name = "DATEINSCRIPTION", nullable = false)
  @NonNull
  private LocalDateTime dateInscription;

  @Column(name = "ISPASSWORDUPDATED", nullable = false)
  @NonNull
  private Boolean isPasswordUpdated;

  @Column(name = "COMMISSION", nullable = false)
  @NonNull
  private Float commission;

  /**
   * Builder pour l'entité Proprietaire.
   *
   * @param nom Nom du proprietaire
   * @param prenom Prénom du proprietaire
   * @param email Email du proprietaire
   * @param numTel Numéro de téléphone du proprietaire
   * @param motDePasse Mot de passe du proprietaire
   * @param dateInscription Date de l'inscription du proprietaire
   * @param isPasswordUpdated Indicateur de modification du mot de passe générer à l'inscription
   */
  @Builder
  public Proprietaire(@NonNull String nom, @NonNull String prenom, @NonNull String email,
      @NonNull String numTel, @NonNull String motDePasse, @NonNull LocalDateTime dateInscription,
      @NonNull Boolean isPasswordUpdated) {
    // Utiliser super() pour appeler le constructeur de la classe parente
    super(nom, prenom, email, numTel, motDePasse);

    // Initialisation des champs propres à Proprietaire
    this.dateInscription = dateInscription;
    // Initialisation de la liste des logements pour éviter NullPointerException lors de
    // l'ajout de logements
    this.logements = List.of();

    this.isPasswordUpdated = isPasswordUpdated;
    this.commission = 20f;
  }


}