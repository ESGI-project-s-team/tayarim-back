package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.amenagement.AmenagementDto;
import fr.esgi.al5.tayarim.dto.regleslogement.ReglesLogementDto;
import fr.esgi.al5.tayarim.entities.Amenagement;
import fr.esgi.al5.tayarim.entities.ReglesLogement;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Classe pour mapper les entités Amenagement en DTOs Amenagement et vice versa.
 */
@Component
public class AmenagementMapper {

  /**
   * Convertit une entité Amenagement en un DTO Amenagement.
   *
   * @param amenagement L'entité Amenagement à convertir.
   * @return Le DTO Amenagement correspondant.
   */
  public static AmenagementDto entityToDto(@NonNull Amenagement amenagement) {
    return new AmenagementDto(
        amenagement.getId(),
        amenagement.getNom(),
        amenagement.getIcone(),
        amenagement.getCategorieAmenagement().getId()
    );
  }

  /**
   * Convertit une liste d'entités Amenagement en une liste de DTOs Amenagement.
   *
   * @param amenagements La liste des Amenagement à convertir.
   * @return Une liste de DTOs Amenagement.
   */
  public static List<AmenagementDto> entityListToDtoList(
      @NonNull List<Amenagement> amenagements) {
    ArrayList<AmenagementDto> amenagementDtos = new ArrayList<>();
    for (Amenagement amenagement : amenagements) {
      amenagementDtos.add(entityToDto(amenagement));
    }
    return amenagementDtos.stream().toList();
  }

}
