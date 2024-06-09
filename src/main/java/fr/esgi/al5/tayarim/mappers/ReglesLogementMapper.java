package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.regleslogement.ReglesLogementDto;
import fr.esgi.al5.tayarim.entities.ReglesLogement;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Classe pour mapper les entités ReglesLogement en DTOs ReglesLogement et vice versa.
 */
@Component
public class ReglesLogementMapper {

  /**
   * Convertit une entité ReglesLogement en un DTO ReglesLogement.
   *
   * @param reglesLogement L'entité regleLogement à convertir.
   * @return Le DTO RegleLogement correspondant.
   */
  public static ReglesLogementDto entityToDto(@NonNull ReglesLogement reglesLogement) {
    return new ReglesLogementDto(
        reglesLogement.getId(),
        reglesLogement.getRegles(),
        reglesLogement.getIcone()
    );
  }

  /**
   * Convertit une liste d'entités ReglesLogement en une liste de DTOs ReglesLogement.
   *
   * @param reglesLogements La liste des regles logements à convertir.
   * @return Une liste de DTOs ReglesLogement.
   */
  public static List<ReglesLogementDto> entityListToDtoList(
      @NonNull List<ReglesLogement> reglesLogements) {
    ArrayList<ReglesLogementDto> reglesLogementsDtos = new ArrayList<>();
    for (ReglesLogement reglesLogement : reglesLogements) {
      reglesLogementsDtos.add(entityToDto(reglesLogement));
    }
    return reglesLogementsDtos.stream().toList();
  }

}
