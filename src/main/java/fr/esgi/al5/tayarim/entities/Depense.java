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

/**
 * Entité représentant une Depense dans le système. représenté par l'entité Depense. La
 * gestion des Depense est effectuée en liaison avec les logement à travers une relation
 * ManyToOne.
 */
@Data
@Entity(name = "DEPENSE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Depense {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "LIBELLE", nullable = false)
  private String libelle;

  @Column(name = "PRIX", nullable = false)
  private Float prix;

  @Column(name = "DATE", nullable = false)
  private LocalDate date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDLOGEMENT", nullable = false)
  private Logement logement;

  /**
   * Constructeur pour l'entité Depense.
   *
   * @param libelle Libelle de la depense
   * @param prix Prix de la depense
   * @param date Date de la depense
   * @param logement Logement associé à la depense
   */
  @Builder
  public Depense(String libelle, Float prix, LocalDate date, Logement logement) {
    this.libelle = libelle;
    this.prix = prix;
    this.date = date;
    this.logement = logement;
  }
}
