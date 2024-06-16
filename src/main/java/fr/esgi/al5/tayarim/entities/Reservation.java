package fr.esgi.al5.tayarim.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Entité représentant une Reservation dans le système. représenté par l'entité Reservation. La
 * gestion des Reservation est effectuée en liaison avec les logement à travers une relation
 * ManyToOne.
 */
@Data
@Entity(name = "RESERVATION")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "IDCOMMANDE", nullable = false)
  private String idCommande;

  @Column(name = "STATUT", nullable = false)
  private String statut;

  @Column(name = "EMAIL", nullable = false)
  private String email;

  @Column(name = "NUMTEL", nullable = false)
  private String numTel;

  @Column(name = "NOM", nullable = false)
  private String nom;

  @Column(name = "PRENOM", nullable = false)
  private String prenom;

  @Column(name = "NBPERSONNES", nullable = false)
  private Integer nbPersonnes;

  @Column(name = "MONTANT", nullable = false)
  private Float montant;

  @Column(name = "CHECKIN", nullable = false)
  private LocalTime checkIn;

  @Column(name = "CHECKOUT", nullable = false)
  private LocalTime checkOut;

  @Column(name = "DATEARRIVEE", nullable = false)
  private LocalDate dateArrivee;

  @Column(name = "DATEDEPART", nullable = false)
  private LocalDate dateDepart;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDLOGEMENT", nullable = false)
  private Logement logement;

  @Column(name = "DATERESERVATION", nullable = false)
  private LocalDateTime dateReservation;

  /**
   * Builder pour l'entité Reservation.
   *
   * @param idCommande      L'identifiant de la commande.
   * @param statut          Le statut de la reservation.
   * @param email           L'email de la personne effectuant la reservation.
   * @param nom             Le nom de la personne effectuant la reservation.
   * @param prenom          Le prenom de la personne effectuant la reservation.
   * @param nbPersonnes     Le nombre de personnes effectuant la reservation.
   * @param montant         Le montant de la reservation.
   * @param checkIn         L'heure d'arrivée de la reservation.
   * @param checkOut        L'heure de départ de la reservation.
   * @param dateArrivee     La date d'arrivée de la reservation.
   * @param dateDepart      La date de départ de la reservation.
   * @param logement        Le logement associé à la reservation.
   * @param dateReservation La date de la reservation.
   *
   */
  @Builder
  public Reservation(@NonNull String idCommande, @NonNull String statut, @NonNull String email,
      @NonNull String numTel, @NonNull String nom, @NonNull String prenom,
      @NonNull Integer nbPersonnes, @NonNull Float montant,
      @NonNull LocalTime checkIn, @NonNull LocalTime checkOut,
      @NonNull LocalDate dateArrivee, @NonNull LocalDate dateDepart,
      @NonNull Logement logement, @NonNull LocalDateTime dateReservation
  ) {
    this.idCommande = idCommande;
    this.statut = statut;
    this.email = email;
    this.numTel = numTel;
    this.nom = nom;
    this.prenom = prenom;
    this.nbPersonnes = nbPersonnes;
    this.montant = montant;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
    this.dateArrivee = dateArrivee;
    this.dateDepart = dateDepart;
    this.logement = logement;
    this.dateReservation = dateReservation;
  }
}