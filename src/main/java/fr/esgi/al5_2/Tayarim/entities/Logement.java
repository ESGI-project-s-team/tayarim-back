package fr.esgi.al5_2.Tayarim.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

/**
 * Entité représentant un logement dans le système. Chaque logement est associé à un propriétaire,
 * représenté par l'entité Proprietaire. La gestion des logements est effectuée en liaison avec les
 * propriétaires à travers une relation ManyToOne.
 */
@Data
@Entity
public class Logement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDPROPRIETAIRE")
  private Proprietaire proprietaire;

  // public Logement(@NonNull Proprietaire proprietaire){
  //     this.proprietaire = proprietaire;
  // }

}
