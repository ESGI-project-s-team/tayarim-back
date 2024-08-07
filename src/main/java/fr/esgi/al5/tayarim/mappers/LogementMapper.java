package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.logement.LogementCreationDto;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.entities.Amenagement;
import fr.esgi.al5.tayarim.entities.ImageLogement;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.entities.ReglesLogement;
import fr.esgi.al5.tayarim.entities.TypeLogement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Classe pour mapper les entités Logement en DTOs Logement et vice versa.
 */
@Component
public class LogementMapper {

  /**
   * Convertit une entité Logement en un DTO Logement.
   *
   * @param logementCreationDto Le Dto logement à convertir.
   * @return Le DTO Logement correspondant.
   */
  public static Logement creationDtoToEntity(@NonNull LogementCreationDto logementCreationDto,
      @NonNull TypeLogement typeLogement,
      @NonNull Proprietaire proprietaire,
      @NonNull Set<ReglesLogement> reglesLogements, @NonNull Set<Amenagement> amenagements,
      @NonNull List<ImageLogement> images) {

    return new Logement(logementCreationDto.getIsLouable(),
        logementCreationDto.getTitre(),
        logementCreationDto.getNombresDeChambres(),
        logementCreationDto.getNombresDeLits(),
        logementCreationDto.getNombresSallesDeBains(),
        logementCreationDto.getCapaciteMaxPersonne(),
        logementCreationDto.getNombresNuitsMin(),
        logementCreationDto.getDescription(),
        0f,
        logementCreationDto.getPrixParNuit(),
        (logementCreationDto.getDefaultCheckIn() == null) ? null
            : LocalTime.parse(logementCreationDto.getDefaultCheckIn()),
        (logementCreationDto.getDefaultCheckOut() == null) ? null
            : LocalTime.parse(logementCreationDto.getDefaultCheckOut()),
        logementCreationDto.getIntervalReservation(),
        logementCreationDto.getVille(),
        logementCreationDto.getAdresse(),
        logementCreationDto.getCodePostal(),
        logementCreationDto.getPays(),
        logementCreationDto.getEtage(),
        logementCreationDto.getNumeroDePorte(),
        typeLogement,
        proprietaire,
        reglesLogements,
        amenagements,
        images);
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
        (logement.getDefaultCheckIn() == null) ? null : logement.getDefaultCheckIn().toString(),
        (logement.getDefaultCheckOut() == null) ? null : logement.getDefaultCheckOut().toString(),
        logement.getAdresse() + ", " + logement.getVille() + ", " + logement.getCodePostal() + ", "
            + logement.getPays(),
        logement.getAdresse(),
        logement.getVille(),
        logement.getCodePostal(),
        logement.getPays(),
        logement.getEtage(),
        logement.getNumeroDePorte(),
        logement.getTypeLogement().getNom(),
        logement.getReglesLogements().stream().collect(
            Collectors.toMap(ReglesLogement::getNom, ReglesLogement::getIcone)
        ),
        logement.getAmenagements().stream().collect(
            Collectors.toMap(Amenagement::getNom, Amenagement::getIcone)
        ),
        ImageLogementMapper.entityListToDtoList(logement.getImages())
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
