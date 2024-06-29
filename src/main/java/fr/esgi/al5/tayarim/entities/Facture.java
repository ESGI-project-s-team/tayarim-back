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
 * Classe représentant une facture.
 */
@Data
@Entity(name = "FACTURE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Facture {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "NUMEROFACTURE", nullable = false)
  private String numeroFacture;

  @Column(name = "DATE", nullable = false)
  private LocalDate dateFacture;

  @Column(name = "MONTANT", nullable = false)
  private Float montant;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDPROPRIETAIRE", nullable = false)
  private Proprietaire proprietaire;


  /**
   * Constructeur par défaut de la classe Facture.
   */
  @Builder
  public Facture(String numeroFacture, LocalDate dateFacture, Float montant,
      Proprietaire proprietaire) {
    this.numeroFacture = numeroFacture;
    this.dateFacture = dateFacture;
    this.montant = montant;
    this.proprietaire = proprietaire;
  }

}
