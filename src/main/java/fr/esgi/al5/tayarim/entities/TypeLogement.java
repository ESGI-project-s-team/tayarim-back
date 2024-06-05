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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDICONE", nullable = false)
  private Icone icone;

}
