package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.logement.LogementCreationDto;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireCandidateDto;
import fr.esgi.al5.tayarim.entities.Amenagement;
import fr.esgi.al5.tayarim.entities.ImageLogement;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.entities.ReglesLogement;
import fr.esgi.al5.tayarim.entities.TypeLogement;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        images,
        true);
  }

  /**
   * Convertit une candidature Logement en une entité Logement.
   */
  public static Logement candidatureDtoToEntity(
      @NonNull ProprietaireCandidateDto proprietaireCandidateDto,
      @NonNull Proprietaire proprietaire,
      @NonNull TypeLogement typeLogement, @NonNull List<ReglesLogement> reglesLogements,
      @NonNull List<Amenagement> amenagements, @NonNull List<ImageLogement> images) {
    return new Logement(
        proprietaireCandidateDto.getIsLouable(),
        proprietaireCandidateDto.getTitre(),
        proprietaireCandidateDto.getNombresDeChambres(),
        proprietaireCandidateDto.getNombresDeLits(),
        proprietaireCandidateDto.getNombresSallesDeBains(),
        proprietaireCandidateDto.getCapaciteMaxPersonne(),
        1,
        proprietaireCandidateDto.getDescription(),
        0f,
        1f,
        LocalTime.parse("15:00"),
        LocalTime.parse("15:00"),
        1,
        proprietaireCandidateDto.getVille(),
        proprietaireCandidateDto.getAddressLogement(),
        proprietaireCandidateDto.getCodePostal(),
        proprietaireCandidateDto.getPays(),
        proprietaireCandidateDto.getEtage(),
        proprietaireCandidateDto.getNumeroDePorte(),
        typeLogement,
        proprietaire,
        new HashSet<>(reglesLogements),
        new HashSet<>(amenagements),
        images,
        false
    );
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
        ImageLogementMapper.entityListToDtoList(logement.getImages()),
        logement.getIsValidated()
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
