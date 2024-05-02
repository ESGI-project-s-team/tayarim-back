package fr.esgi.al5_2.Tayarim.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import fr.esgi.al5_2.Tayarim.dto.logement.LogementDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.entities.Logement;
import lombok.NonNull;

/**
 * Classe pour mapper les entités Logement en DTOs Logement et vice versa.
 */
@Component
public class LogementMapper {

  /**
   * Convertit une entité Logement en un DTO Logement.
   *
   * @param logement L'entité logement à convertir.
   * @return Le DTO Logement correspondant.
   */
  public static LogementDTO entityToDto(@NonNull Logement logement) {
    return new LogementDTO(
        logement.getId(),
        logement.getProprietaire().getId());
  }

  /**
   * Convertit une liste d'entités Logement en une liste de DTOs Logement.
   *
   * @param logements La liste des logements à convertir.
   * @return Une liste de DTOs Logement.
   */
  public static List<LogementDTO> entityListToDtoList(@NonNull List<Logement> logements) {
    ArrayList<LogementDTO> logementDtos = new ArrayList<>();
    for (Logement logement : logements) {
      logementDtos.add(entityToDto(logement));
    }
    return logementDtos.stream().toList();
  }

}
