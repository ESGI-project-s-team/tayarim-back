package fr.esgi.al5.tayarim.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
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
  private String date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDLOGEMENT", nullable = false)
  private Logement logement;

}
