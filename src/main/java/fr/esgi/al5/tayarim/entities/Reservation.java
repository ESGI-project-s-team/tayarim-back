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
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Entité représentant une Reservation dans le système. représenté par l'entité Reservation. La gestion des Reservation est
 * effectuée en liaison avec les logement à travers une relation ManyToOne.
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDLOGEMENT", nullable = false)
  private Logement logement;

  /**
   * Builder pour l'entité Reservation.
   *
   * @param proprietaire         Propriétaire du logement
   */
  @Builder
  public Reservation(@NonNull String idCommande, @NonNull String statut, @NonNull String email, 
    @NonNull String nom, @NonNull String prenom, @NonNull Integer nbPersonnes, 
    @NonNull Float montant, @NonNull LocalTime checkIn, @NonNull LocalTime checkOut, 
    @NonNull Logement logement 
  ) {
    this.idCommande = idCommande;
    this.statut = statut;
    this.email = email;
    this.nom = nom;
    this.prenom = prenom;
    this.nbPersonnes = nbPersonnes;
    this.montant = montant;
    this.checkIn = checkIn;
    this.checkOut = checkOut;
    this.logement = logement;
  }
}
