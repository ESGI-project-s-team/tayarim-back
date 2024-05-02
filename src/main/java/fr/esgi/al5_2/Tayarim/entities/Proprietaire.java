package fr.esgi.al5_2.Tayarim.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

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

  @Builder
  public Proprietaire(@NonNull String nom, @NonNull String prenom, @NonNull String email,
      @NonNull String numTel, @NonNull String motDePasse, @NonNull LocalDateTime dateInscription,
      @NonNull Boolean isPasswordUpdated) {
    // Utiliser super() pour appeler le constructeur de la classe parente
    super(nom, prenom, email, numTel, motDePasse);

    // Initialisation des champs propres à Proprietaire
    this.dateInscription = dateInscription;
    // Initialisation de la liste des logements pour éviter NullPointerException lors de l'ajout de logements
    this.logements = List.of();

    this.isPasswordUpdated = isPasswordUpdated;
  }


}