package fr.esgi.al5.tayarim.services;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.al5.tayarim.auth.JwtHelper;
import fr.esgi.al5.tayarim.auth.TokenCacheService;
import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.dto.auth.AuthLoginResponseDto;
import fr.esgi.al5.tayarim.dto.auth.AuthRefreshResponseDto;
import fr.esgi.al5.tayarim.dto.auth.AuthResponseDto;
import fr.esgi.al5.tayarim.dto.logement.LogementCreationDto;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.dto.logement.LogementUpdateDto;
import fr.esgi.al5.tayarim.dto.proprietaire.AdministrateurDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireUpdateDto;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.exceptions.AdministrateurNotFoundException;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.exceptions.TokenExpireOrInvalidException;
import fr.esgi.al5.tayarim.exceptions.UtilisateurNotFoundException;
import fr.esgi.al5.tayarim.mappers.LogementMapper;
import fr.esgi.al5.tayarim.mappers.ProprietaireMapper;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe de service gérant les logements.
 */
@Service
@Transactional(readOnly = true)
public class LogementService {

  private final LogementRepository logementRepository;
  private final ProprietaireRepository proprietaireRepository;


  /**
   * Constructeur pour le service de logement.
   *
   * @param logementRepository Le repository des logements.
   */
  public LogementService(LogementRepository logementRepository,
      ProprietaireRepository proprietaireRepository) {
    this.logementRepository = logementRepository;
    this.proprietaireRepository = proprietaireRepository;
  }

  /**
   * Tente de créer un logement.
   *
   * @param logementCreationDto Le dto de création de logement.
   * @return {@link LogementDto}
   */
  @Transactional
  public LogementDto createLogement(@NonNull LogementCreationDto logementCreationDto) {

    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(
        logementCreationDto.getIdProprietaire());

    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    Proprietaire proprietaire = optionalProprietaire.get();

    return LogementMapper.entityToDto(
        logementRepository.save(
            LogementMapper.creationDtoToEntity(
                logementCreationDto,
                1L,
                1L,
                proprietaire)
        )
    );
  }

  /**
   * Tente de récupérer tous les logements.
   *
   * @return {@link LogementDto}
   */
  @Transactional
  public List<LogementDto> getAllLogement(@NonNull Long idUser, @NonNull Boolean isAdmin) {
    if (isAdmin) {
      return LogementMapper.entityListToDtoList(
          logementRepository.findAll()
      );
    }
    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(idUser);
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    return LogementMapper.entityListToDtoList(
        logementRepository.findAllByProprietaire(optionalProprietaire.get())
    );
  }

  /**
   * Tente de récupérer un logements par son id.
   *
   * @return {@link LogementDto}
   */
  @Transactional
  public LogementDto getLogementById(@NonNull Long id) {
    Optional<Logement> optionalLogement = logementRepository.findById(id);

    if (optionalLogement.isEmpty()) {
      throw new LogementNotFoundException();
    }

    return LogementMapper.entityToDto(optionalLogement.get());
  }

  /**
   * Met à jour les informations d'un logement existant.
   *
   * @param id                L'identifiant du logement à mettre à jour.
   * @param logementUpdateDto Les nouvelles informations du logement.
   * @return Le DTO du logement mis à jour.
   * @throws ProprietaireInvalidUpdateBody si les données de mise à jour sont invalides.
   */
  @Transactional
  public LogementDto updateLogement(@NonNull Long id,
      @NonNull LogementUpdateDto logementUpdateDto) {

    if ((logementUpdateDto.getTitre() == null || logementUpdateDto.getTitre().isBlank())
        && (logementUpdateDto.getNombresDeChambres() == null
        || logementUpdateDto.getNombresDeChambres() == 0)
        && (logementUpdateDto.getNombresDeLits() == null
        || logementUpdateDto.getNombresDeLits() == 0)
        && (logementUpdateDto.getNombresSallesDeBains() == null
        || logementUpdateDto.getNombresSallesDeBains() == 0)
        && (logementUpdateDto.getCapaciteMaxPersonne() == null
        || logementUpdateDto.getCapaciteMaxPersonne() == 0)
        && (logementUpdateDto.getNombresNuitsMin() == null
        || logementUpdateDto.getNombresNuitsMin() == 0)
        && (logementUpdateDto.getDescription() == null || logementUpdateDto.getDescription()
        .isBlank())
        && (logementUpdateDto.getPrixParNuit() == null || logementUpdateDto.getPrixParNuit() == 0)
        && (logementUpdateDto.getDefaultCheckIn() == null || logementUpdateDto.getDefaultCheckIn()
        .isBlank())
        && (logementUpdateDto.getDefaultCheckOut() == null || logementUpdateDto.getDefaultCheckOut()
        .isBlank())
        && (logementUpdateDto.getIntervalReservation() == null
        || logementUpdateDto.getIntervalReservation() == 0)
        && (logementUpdateDto.getIdTypeLogement() == null
        || logementUpdateDto.getIdTypeLogement() == 0)) {
      throw new LogementInvalidUpdateBody();
    }

    Optional<Logement> optionalLogement = logementRepository.findById(id);

    if (optionalLogement.isEmpty()) {
      throw new LogementNotFoundException();
    }

    Logement logement = optionalLogement.get();

    logement.setTitre(logementUpdateDto.getTitre().isBlank() ? logement.getTitre()
        : logementUpdateDto.getTitre());
    logement.setNombresDeChambres(
        logementUpdateDto.getNombresDeChambres() == null ? logement.getNombresDeChambres()
            : logementUpdateDto.getNombresDeChambres());
    logement.setNombresDeLits(
        logementUpdateDto.getNombresDeLits() == null ? logement.getNombresDeLits()
            : logementUpdateDto.getNombresDeLits());
    logement.setNombresSallesDeBains(
        logementUpdateDto.getNombresSallesDeBains() == null ? logement.getNombresSallesDeBains()
            : logementUpdateDto.getNombresSallesDeBains());
    logement.setCapaciteMaxPersonne(
        logementUpdateDto.getCapaciteMaxPersonne() == null ? logement.getCapaciteMaxPersonne()
            : logementUpdateDto.getCapaciteMaxPersonne());
    logement.setNombresNuitsMin(
        logementUpdateDto.getNombresNuitsMin() == null ? logement.getNombresNuitsMin()
            : logementUpdateDto.getNombresNuitsMin());
    logement.setDescription(logementUpdateDto.getDescription().isBlank() ? logement.getDescription()
        : logementUpdateDto.getDescription());
    logement.setPrixParNuit(logementUpdateDto.getPrixParNuit() == null ? logement.getPrixParNuit()
        : logementUpdateDto.getPrixParNuit());
    logement.setDefaultCheckIn(
        logementUpdateDto.getDefaultCheckIn().isBlank() ? logement.getDefaultCheckIn()
            : LocalTime.parse(logementUpdateDto.getDefaultCheckIn()));
    logement.setDefaultCheckOut(
        logementUpdateDto.getDefaultCheckOut().isBlank() ? logement.getDefaultCheckOut()
            : LocalTime.parse(logementUpdateDto.getDefaultCheckOut()));
    logement.setIntervalReservation(
        logementUpdateDto.getIntervalReservation() == null ? logement.getIntervalReservation()
            : logementUpdateDto.getIntervalReservation());
    //update adresse
    logement.setIdTypeLogement(
        logementUpdateDto.getIdTypeLogement() == null ? logement.getIdTypeLogement()
            : logementUpdateDto.getIdTypeLogement());

    return LogementMapper.entityToDto(logementRepository.save(logement));

  }

  /**
   * Supprime un logement par son identifiant.
   *
   * @param id L'identifiant du logement à supprimer.
   * @return Le DTO du logement supprimé.
   * @throws LogementNotFoundException si le logement à supprimer n'est pas trouvé.
   */
  @Transactional
  public LogementDto deleteLogement(@NonNull Long id) {

    Optional<Logement> optionalLogement = logementRepository.findById(id);
    if (optionalLogement.isEmpty()) {
      throw new LogementNotFoundException();
    }

    LogementDto logementDto = LogementMapper.entityToDto(optionalLogement.get());

    logementRepository.deleteById(id);

    return logementDto;

  }


}
