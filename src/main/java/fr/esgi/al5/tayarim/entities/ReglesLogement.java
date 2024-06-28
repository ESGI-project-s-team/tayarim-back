package fr.esgi.al5.tayarim.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entité représentant les règles de logement dans le système. Chaque règle de logement est associée
 * à un ou plusieurs logements, représentés par l'entité Logement. La gestion des règles de logement
 * est effectuée en liaison avec les logements à travers une relation ManyToMany.
 */
@Data
@Entity(name = "REGLESLOGEMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = {"id", "logements"})
public class ReglesLogement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(length = 100, nullable = false, name = "NOM")
  private String nom;

  @Column(length = 1000, nullable = false, name = "ICONE")
  private String icone;

  @ManyToMany(mappedBy = "reglesLogements")
  private Set<Logement> logements;

}