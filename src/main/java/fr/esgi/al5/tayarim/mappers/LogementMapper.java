package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.logement.LogementCreationDto;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Classe pour mapper les entités Logement en DTOs Logement et vice versa.
 */
@Component
public class LogementMapper {

  private ProprietaireRepository proprietaireRepository;

  /**
   * Convertit une entité Logement en un DTO Logement.
   *
   * @param logement L'entité logement à convertir.
   * @return Le DTO Logement correspondant.
   */
  public static Logement creationDtoToEntity(@NonNull Boolean isLouable,
      @NonNull Integer nombresDeChambres, @NonNull Integer nombresDeLits,
      @NonNull Integer nombresSallesDeBains,
      @NonNull Integer capaciteMaxPersonne, @NonNull Integer nombresNuitsMin,
      @NonNull String description, @NonNull Float note, @NonNull Float prixParNuit,
      @NonNull LocalDateTime defaultCheckIn, @NonNull LocalDateTime defaultCheckOut,
      @NonNull Integer intervalReservation, @NonNull Long idAdresse,
      @NonNull Long idTypeLogement, @NonNull Proprietaire proprietaire) {



    return new Logement(isLouable, nombresDeChambres, nombresDeLits, nombresSallesDeBains,
        capaciteMaxPersonne, nombresNuitsMin, description, note, prixParNuit, defaultCheckIn,
        defaultCheckOut, intervalReservation, idAdresse, idTypeLogement, proprietaire);
  }

  /**
   * Convertit une entité Logement en un DTO Logement.
   *
   * @param logement L'entité logement à convertir.
   * @return Le DTO Logement correspondant.
   */
  public static LogementDto entityToDto(@NonNull Logement logement) {
    return new LogementDto(
        logement.getId(),
        logement.getProprietaire().getId());
  }

  /**
   * Convertit une liste d'entités Logement en une liste de DTOs Logement.
   *
   * @param logements La liste des logements à convertir.
   * @return Une liste de DTOs Logement.
   */
  public static List<LogementDto> entityListToDtoList(@NonNull List<Logement> logements) {
    ArrayList<LogementDto> logementDtos = new ArrayList<>();
    for (Logement logement : logements) {
      logementDtos.add(entityToDto(logement));
    }
    return logementDtos.stream().toList();
  }

}
