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
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Entité représentant les images de logement dans le système. Chaque image de logement est associée
 * un ou plusieurs logements, représentés par l'entité Logement. La gestion des images de logement
 * est effectuée en liaison avec les logements à travers une relation ManyToOne.
 */
@Data
@Entity(name = "IMAGELOGEMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageLogement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(length = 1000, nullable = false, name = "URL")
  private String url;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "IDLOGEMENT", nullable = false)
  private Logement logement;

  @Column(name = "ISMAINIMAGE", nullable = false)
  private Boolean isMainImage;

  /**
   * Builder pour l'entitée ImageLogement
   *
   * @param url         URL de l'image
   * @param logement    Logement associé à l'image
   * @param isMainImage Boolean indiquant si l'image est l'image principale du logement
   */
  @Builder
  public ImageLogement(@NonNull String url, @NonNull Logement logement,
      @NonNull Boolean isMainImage) {
    this.url = url;
    this.logement = logement;
    this.isMainImage = isMainImage;
  }

}