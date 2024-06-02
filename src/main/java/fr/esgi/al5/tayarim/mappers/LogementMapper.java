package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.logement.LogementCreationDto;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
   * @param logementCreationDto Le Dto logement à convertir.
   * @return Le DTO Logement correspondant.
   */
  public static Logement creationDtoToEntity(@NonNull LogementCreationDto logementCreationDto,
      @NonNull Long idAddresse, @NonNull Long idTypeLogement, @NonNull Proprietaire proprietaire) {

    return new Logement(true,
        logementCreationDto.getTitre(),
        logementCreationDto.getNombresDeChambres(),
        logementCreationDto.getNombresDeLits(),
        logementCreationDto.getNombresSallesDeBains(),
        logementCreationDto.getCapaciteMaxPersonne(),
        logementCreationDto.getNombresNuitsMin(),
        logementCreationDto.getDescription(),
        0f,
        logementCreationDto.getPrixParNuit(),
        LocalTime.parse(logementCreationDto.getDefaultCheckIn()),
        LocalTime.parse(logementCreationDto.getDefaultCheckOut()),
        logementCreationDto.getIntervalReservation(),
        idAddresse,
        idTypeLogement,
        proprietaire);
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
        logement.getTitre(),
        logement.getProprietaire().getId(),
        logement.getIsLouable(),
        logement.getNombresDeChambres(),
        logement.getNombresDeLits(),
        logement.getNombresSallesDeBains(),
        logement.getCapaciteMaxPersonne(),
        logement.getNombresNuitsMin(),
        logement.getDescription(),
        logement.getNote(),
        logement.getPrixParNuit(),
        logement.getDefaultCheckIn().toString(),
        logement.getDefaultCheckOut().toString(),
        "13 FakeStreet City, 12345 Country",
        "13 FakeStreet",
        "City",
        "12345",
        "Country",
        null,
        null,
        "Appartement"
    );
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
