package fr.esgi.al5.tayarim.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Entité représentant une CategorieAmenagement dans le système. Chaque CategorieAmenagement peut
 * etre associé à plusieur Amenagement, représenté par l'entité Amenagement. La gestion des
 * CategorieAmenagement est effectuée en liaison avec les Amenagement à travers une relation
 * ManyToOne.
 */
@Data
@Entity(name = "CATEGORIEAMENAGEMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategorieAmenagement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "NOM", nullable = false)
  private String nom;

  @OneToMany(mappedBy = "categorieAmenagement", cascade = CascadeType.ALL)
  private List<Amenagement> amenagements;

}
