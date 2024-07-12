package fr.esgi.al5.tayarim.entities;

import com.google.cloud.storage.Blob;
import fr.esgi.al5.tayarim.TayarimApplication;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreRemove;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
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
  private Long id;

  @Column(length = 1000, nullable = false, name = "URL")
  private String url;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "IDLOGEMENT", nullable = false)
  private Logement logement;

  @Column(name = "ISMAINIMAGE", nullable = false)
  private Boolean isMainImage;

  /**
   * Builder pour l'entitée ImageLogement.
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

  /**
   * Méthode appelée avant la suppression d'une image de logement.
   */
  @PreRemove()
  public void beforeDelete() {
    int index = this.url.indexOf("House%20images/");
    if (index == -1) {
      index = this.url.indexOf("House images/");
    }

    String filePath = this.url.substring(index);

    Blob blob = TayarimApplication.bucket.get(filePath);

    if (blob != null) {
      blob.delete();
    }
  }

}