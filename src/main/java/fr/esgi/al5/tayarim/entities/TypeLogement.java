package fr.esgi.al5.tayarim.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité représentant un type delogement dans le système. Chaque type de logement peut etre associé
 * à plusieur logement, représenté par l'entité Logement. La gestion des type de logement est
 * effectuée en liaison avec les logement à travers une relation ManyToOne.
 */
@Data
@Entity(name = "TYPELOGEMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TypeLogement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "NOM")
  private String nom;

  @Column(name = "ICONE", nullable = false)
  private String icone;

}
