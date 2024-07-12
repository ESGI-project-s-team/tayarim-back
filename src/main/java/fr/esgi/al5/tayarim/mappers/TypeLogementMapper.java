package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.logement.TypeLogementDto;
import fr.esgi.al5.tayarim.entities.TypeLogement;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Classe pour mapper les entités de Type de Logement en DTOs TypeDeLogement et vice versa.
 */
@Component
public class TypeLogementMapper {

  /**
   * Convertit une entité TypeLogement en un DTO TypeLogement.
   *
   * @param typeLogement L'entité TypeLogement à convertir.
   * @return Le DTO TypeLogement correspondant.
   */
  public static TypeLogementDto entityToDto(@NonNull TypeLogement typeLogement) {
    return new TypeLogementDto(
        typeLogement.getId(),
        typeLogement.getNom(),
        typeLogement.getIcone()
    );
  }

  /**
   * Convertit une liste d'entités TypeLogement en une liste de DTOs TypeLogement.
   *
   * @param typeLogements La liste des type de logements à convertir.
   * @return Une liste de DTOs TypeLogement.
   */
  public static List<TypeLogementDto> entityListToDtoList(
      @NonNull List<TypeLogement> typeLogements) {
    ArrayList<TypeLogementDto> typeLogementDtos = new ArrayList<>();
    for (TypeLogement typeLogement : typeLogements) {
      typeLogementDtos.add(entityToDto(typeLogement));
    }
    return typeLogementDtos.stream().toList();
  }

}
