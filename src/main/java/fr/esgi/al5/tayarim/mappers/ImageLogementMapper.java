package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.logement.ImageLogementDto;
import fr.esgi.al5.tayarim.dto.logement.TypeLogementDto;
import fr.esgi.al5.tayarim.entities.ImageLogement;
import fr.esgi.al5.tayarim.entities.TypeLogement;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Classe pour mapper les entités de ImageLogement en DTOs ImageLogement et vice versa.
 */
@Component
public class ImageLogementMapper {

  /**
   * Convertit une entité ImageLogement en un DTO ImageLogement.
   *
   * @param imageLogement L'entité ImageLogement à convertir.
   * @return Le DTO ImageLogement correspondant.
   */
  public static ImageLogementDto entityToDto(@NonNull ImageLogement imageLogement) {
    return new ImageLogementDto(
        imageLogement.getId(),
        "https://storage.googleapis.com/tayarim-tf-storage/".concat(imageLogement.getUrl()),
        imageLogement.getIsMainImage()
    );
  }

  /**
   * Convertit une liste d'entités ImageLogement en une liste de DTOs ImageLogement.
   *
   * @param imageLogements La liste des ImageLogement à convertir.
   * @return Une liste de DTOs ImageLogement.
   */
  public static List<ImageLogementDto> entityListToDtoList(
      @NonNull List<ImageLogement> imageLogements) {
    ArrayList<ImageLogementDto> imageLogementDtos = new ArrayList<>();
    for (ImageLogement imageLogement : imageLogements) {
      imageLogementDtos.add(entityToDto(imageLogement));
    }
    return imageLogementDtos.stream().toList();
  }

}
