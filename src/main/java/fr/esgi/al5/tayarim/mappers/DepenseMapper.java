package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.depense.DepenseCreationDto;
import fr.esgi.al5.tayarim.dto.depense.DepenseDto;
import fr.esgi.al5.tayarim.dto.logement.TypeLogementDto;
import fr.esgi.al5.tayarim.entities.Depense;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.TypeLogement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Classe pour mapper les entités de Type de Logement en DTOs TypeDeLogement et vice versa.
 */
@Component
public class DepenseMapper {

  /**
   * Convertit un DTO de création de Depense en une entité Depense.
   *
   * @return L'entité Depense correspondant.
   */
  public static Depense creationDtoToEntity(@NonNull DepenseCreationDto depenseCreationDto,
      @NonNull LocalDate date, @NonNull Logement logement) {
    return new Depense(
        depenseCreationDto.getLibelle(),
        depenseCreationDto.getPrix(),
        date,
        logement
    );
  }

  /**
   * Convertit une entité Depense en un DTO Depense.
   *
   * @param depense L'entité Depense à convertir.
   * @return Le DTO Depense correspondant.
   */
  public static DepenseDto entityToDto(@NonNull Depense depense) {
    return new DepenseDto(
        depense.getId(),
        depense.getLibelle(),
        depense.getPrix(),
        depense.getDate().toString(),
        depense.getLogement().getId(),
        depense.getLogement().getTitre()
    );
  }

  /**
   * Convertit une liste d'entités Depense en une liste de DTOs Depense.
   *
   * @param depenses La liste des Depense à convertir.
   * @return Une liste de DTOs Depense.
   */
  public static List<DepenseDto> entityListToDtoList(
      @NonNull List<Depense> depenses) {
    ArrayList<DepenseDto> depenseDtos = new ArrayList<>();
    for (Depense depense : depenses) {
      depenseDtos.add(entityToDto(depense));
    }
    return depenseDtos.stream().toList();
  }

}
