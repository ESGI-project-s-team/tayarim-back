package fr.esgi.al5.tayarim.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entité représentant un Amenagement dans le système. Chaque Amenagement peut etre associé à
 * plusieur logement, représenté par l'entité Logement. La gestion des Amenagement est effectuée en
 * liaison avec les logement à travers une relation ManyToOne.
 */
@Data
@Entity(name = "AMENAGEMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = {"id", "logements"})
public class Amenagement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "NOM", nullable = false)
  private String nom;

  @Column(name = "ICONE", nullable = false)
  private String icone;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDCATEGORIEAMENAGEMENT", nullable = false)
  private CategorieAmenagement categorieAmenagement;

  @ManyToMany(mappedBy = "amenagements")
  private Set<Logement> logements;

}
