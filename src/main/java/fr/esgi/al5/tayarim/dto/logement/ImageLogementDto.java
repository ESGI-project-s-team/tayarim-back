package fr.esgi.al5.tayarim.dto.logement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Objet de Transfert de Données (DTO) représentant une imagelogement dans le système. Ce DTO est
 * utilisé pour encapsuler les informations essentielles d'une imagelogement.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
public class ImageLogementDto {

  @NonNull
  private Long id;

  @NonNull
  private String url;

  @NonNull
  private Boolean isMainImage;

}
