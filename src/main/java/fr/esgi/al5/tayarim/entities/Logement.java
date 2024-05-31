package fr.esgi.al5.tayarim.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Entité représentant un logement dans le système. Chaque logement est associé à un propriétaire,
 * représenté par l'entité Proprietaire. La gestion des logements est effectuée en liaison avec les
 * propriétaires à travers une relation ManyToOne.
 */
@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Logement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "LOUABLE")
  private Boolean isLouable;

  @Column(name = "NOMBRESDECHAMBRES")
  private Integer nombresDeChambres;

  @Column(name = "NOMBRESDELITS")
  private Integer nombresDeLits;

  @Column(name = "NOMBRESSALLESDEBAINS")
  private Integer nombresSallesDeBains;

  @Column(name = "CAPACITEMAXPERSONNE")
  private Integer capaciteMaxPersonne;

  @Column(name = "NOMBRESNUITSMIN")
  private Integer nombresNuitsMin;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "NOTE")
  private Float note;

  @Column(name = "PRIXPARNUIT")
  private Float prixParNuit;

  @Column(name = "DEFAULTCHECKIN")
  private LocalTime defaultCheckIn;

  @Column(name = "DEFAULTCHECKOUT")
  private LocalTime defaultCheckOut;

  @Column(name = "INTERVALRESERVATION")
  private Integer intervalReservation;

  @Column(name = "IDADRESSE")
  private Long idAdresse;

  @Column(name = "IDTYPELOGEMENT")
  private Long idTypeLogement;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDPROPRIETAIRE")
  private Proprietaire proprietaire;

  /**
   * Builder pour l'entité Logmeent.
   *
   * @param isLouable Indicateur de disponibilité du logement
   * @param nombresDeChambres Nombre de chambres dans le logement
   * @param nombresDeLits Nombre de lits dans le logement
   * @param nombresSallesDeBains Nombre de salles de bains dans le logement
   * @param capaciteMaxPersonne Capacité maximale de personnes dans le logement
   * @param nombresNuitsMin Nombre minimum de nuits pour une réservation
   * @param description Description du logement
   * @param note Note moyenne du logement
   * @param prixParNuit Prix par nuit du logement
   * @param defaultCheckIn Heure de check-in par défaut
   * @param defaultCheckOut Heure de check-out par défaut
   * @param intervalReservation Intervalle de réservation en jours
   * @param idAdresse Identifiant de l'adresse du logement
   * @param idTypeLogement Identifiant du type de logement
   * @param proprietaire Propriétaire du logement
   */
  @Builder
  public Logement(@NonNull Boolean isLouable, @NonNull Integer nombresDeChambres,
      @NonNull Integer nombresDeLits, @NonNull Integer nombresSallesDeBains,
      @NonNull Integer capaciteMaxPersonne, @NonNull Integer nombresNuitsMin,
      @NonNull String description, @NonNull Float note, @NonNull Float prixParNuit,
      @NonNull LocalTime defaultCheckIn, @NonNull LocalTime defaultCheckOut,
      @NonNull Integer intervalReservation, @NonNull Long idAdresse,
      @NonNull Long idTypeLogement, @NonNull Proprietaire proprietaire) {
    this.isLouable = isLouable;
    this.nombresDeChambres = nombresDeChambres;
    this.nombresDeLits = nombresDeLits;
    this.nombresSallesDeBains = nombresSallesDeBains;
    this.capaciteMaxPersonne = capaciteMaxPersonne;
    this.nombresNuitsMin = nombresNuitsMin;
    this.description = description;
    this.note = note;
    this.prixParNuit = prixParNuit;
    this.defaultCheckIn = defaultCheckIn;
    this.defaultCheckOut = defaultCheckOut;
    this.intervalReservation = intervalReservation;
    this.idAdresse = idAdresse;
    this.idTypeLogement = idTypeLogement;
    this.proprietaire = proprietaire;
  }

}
