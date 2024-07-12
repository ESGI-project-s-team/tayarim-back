package fr.esgi.al5.tayarim.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Entité représentant une Indisponibilite dans le système. représenté par l'entité Indisponibilite.
 * La gestion des Indisponibilite est effectuée en liaison avec les logement à travers une relation
 * ManyToOne.
 */
@Data
@Entity(name = "INDISPONIBILITE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Indisponibilite {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "DATEDEBUT", nullable = false)
  private LocalDate dateDebut;

  @Column(name = "DATEFIN", nullable = false)
  private LocalDate dateFin;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDLOGEMENT", nullable = false)
  private Logement logement;

  /**
   * Builder pour l'entité Indisponibilité.
   *
   * @param dateDebut La date de début de l'indisponibilité.
   * @param dateFin   La date de fin de l'indisponibilité.
   * @param logement  Le logement concerné par l'indisponibilité.
   */
  @Builder
  public Indisponibilite(@NonNull LocalDate dateDebut, @NonNull LocalDate dateFin,
      @NonNull Logement logement) {
    this.dateDebut = dateDebut;
    this.dateFin = dateFin;
    this.logement = logement;
  }

}
