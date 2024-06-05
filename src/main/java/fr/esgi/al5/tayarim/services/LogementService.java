package fr.esgi.al5.tayarim.services;


import fr.esgi.al5.tayarim.dto.logement.LogementCreationDto;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.dto.logement.LogementUpdateDto;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.entities.TypeLogement;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidCreationBody;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidTypeLogement;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.mappers.LogementMapper;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import fr.esgi.al5.tayarim.repositories.TypeLogementRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe de service gérant les logements.
 */
@Service
@Transactional(readOnly = true)
public class LogementService {

  private final LogementRepository logementRepository;
  private final ProprietaireRepository proprietaireRepository;

  private final TypeLogementRepository typeLogementRepository;


  /**
   * Constructeur pour le service de logement.
   *
   * @param logementRepository     Le repository des logements.
   * @param proprietaireRepository Le repository des propriétaires.
   * @param typeLogementRepository Le repository des types de logements.
   */
  public LogementService(LogementRepository logementRepository,
      ProprietaireRepository proprietaireRepository,
      TypeLogementRepository typeLogementRepository) {
    this.logementRepository = logementRepository;
    this.proprietaireRepository = proprietaireRepository;
    this.typeLogementRepository = typeLogementRepository;
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

    if (
        logementCreationDto.getIsLouable() && (
            logementCreationDto.getNombresDeChambres() == null
                || logementCreationDto.getNombresDeLits() == null
                || logementCreationDto.getNombresSallesDeBains() == null
                || logementCreationDto.getCapaciteMaxPersonne() == null
                || logementCreationDto.getNombresNuitsMin() == null
                || (logementCreationDto.getDefaultCheckIn() == null
                || logementCreationDto.getDefaultCheckIn().isBlank())
                || (logementCreationDto.getDefaultCheckOut() == null
                || logementCreationDto.getDefaultCheckOut().isBlank())
                || logementCreationDto.getPrixParNuit() == null
        )) {
      throw new LogementInvalidCreationBody();
    }

    Optional<TypeLogement> optionalTypeLogement = typeLogementRepository.findById(
        logementCreationDto.getIdTypeLogement());
    if (optionalTypeLogement.isEmpty()) {
      throw new LogementInvalidTypeLogement();
    }

    return LogementMapper.entityToDto(
        logementRepository.save(
            LogementMapper.creationDtoToEntity(
                logementCreationDto,
                optionalTypeLogement.get(),
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
        && (logementUpdateDto.getIsLouable() == null)
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
        && (logementUpdateDto.getVille() == null || logementUpdateDto.getVille().isBlank())
        && (logementUpdateDto.getAdresse() == null || logementUpdateDto.getAdresse().isBlank())
        && (logementUpdateDto.getCodePostal() == null || logementUpdateDto.getCodePostal()
        .isBlank())
        && (logementUpdateDto.getPays() == null || logementUpdateDto.getPays().isBlank())
        && (logementUpdateDto.getEtage() == null || logementUpdateDto.getEtage().isBlank())
        && (logementUpdateDto.getNumeroDePorte() == null || logementUpdateDto.getNumeroDePorte()
        .isBlank())
        && (logementUpdateDto.getIdTypeLogement() == null
        || logementUpdateDto.getIdTypeLogement() == 0)) {
      throw new LogementInvalidUpdateBody();
    }

    Optional<Logement> optionalLogement = logementRepository.findById(id);

    if (optionalLogement.isEmpty()) {
      throw new LogementNotFoundException();
    }

    Logement logement = optionalLogement.get();

    if (
        (!logement.getIsLouable() && logementUpdateDto.getIsLouable())
            && (
            (logement.getNombresDeChambres() == null
                && logementUpdateDto.getNombresDeChambres() == null)
                || (logement.getNombresDeLits() == null
                && logementUpdateDto.getNombresDeLits() == null)
                || (logement.getNombresSallesDeBains() == null
                && logementUpdateDto.getNombresSallesDeBains() == null)
                || (logement.getCapaciteMaxPersonne() == null
                && logementUpdateDto.getCapaciteMaxPersonne() == null)
                || (logement.getNombresNuitsMin() == null
                && logementUpdateDto.getNombresNuitsMin() == null)
                || (logement.getDefaultCheckIn() == null
                && logementUpdateDto.getDefaultCheckIn() == null)
                || (logement.getDefaultCheckOut() == null
                && logementUpdateDto.getDefaultCheckOut() == null)
                || (logement.getPrixParNuit() == null && logementUpdateDto.getPrixParNuit() == null)
        )
    ) {
      throw new LogementInvalidUpdateBody();
    }

    logement.setTitre(
        (logementUpdateDto.getTitre() == null || logementUpdateDto.getTitre().isBlank())
            ? logement.getTitre()
            : logementUpdateDto.getTitre());
    logement.setIsLouable(
        logementUpdateDto.getIsLouable() == null ? logement.getIsLouable()
            : logementUpdateDto.getIsLouable());
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
    logement.setDescription(
        logementUpdateDto.getDescription() == null || logementUpdateDto.getDescription().isBlank()
            ? logement.getDescription()
            : logementUpdateDto.getDescription());
    logement.setPrixParNuit(logementUpdateDto.getPrixParNuit() == null ? logement.getPrixParNuit()
        : logementUpdateDto.getPrixParNuit());
    logement.setDefaultCheckIn(
        logementUpdateDto.getDefaultCheckIn() == null || logementUpdateDto.getDefaultCheckIn()
            .isBlank() ? logement.getDefaultCheckIn()
            : LocalTime.parse(logementUpdateDto.getDefaultCheckIn()));
    logement.setDefaultCheckOut(
        logementUpdateDto.getDefaultCheckOut() == null || logementUpdateDto.getDefaultCheckOut()
            .isBlank() ? logement.getDefaultCheckOut()
            : LocalTime.parse(logementUpdateDto.getDefaultCheckOut()));
    logement.setIntervalReservation(
        logementUpdateDto.getIntervalReservation() == null ? logement.getIntervalReservation()
            : logementUpdateDto.getIntervalReservation());
    logement.setVille(
        logementUpdateDto.getVille() == null || logementUpdateDto.getVille().isBlank()
            ? logement.getVille()
            : logementUpdateDto.getVille());
    logement.setAdresse(
        logementUpdateDto.getAdresse() == null || logementUpdateDto.getAdresse().isBlank()
            ? logement.getAdresse()
            : logementUpdateDto.getAdresse());
    logement.setCodePostal(
        logementUpdateDto.getCodePostal() == null || logementUpdateDto.getCodePostal().isBlank()
            ? logement.getCodePostal()
            : logementUpdateDto.getCodePostal());
    logement.setPays(
        logementUpdateDto.getPays() == null || logementUpdateDto.getPays().isBlank()
            ? logement.getPays()
            : logementUpdateDto.getPays());
    logement.setEtage(
        logementUpdateDto.getEtage() == null || logementUpdateDto.getEtage().isBlank()
            ? logement.getEtage()
            : logementUpdateDto.getEtage());
    logement.setNumeroDePorte(
        logementUpdateDto.getNumeroDePorte() == null || logementUpdateDto.getNumeroDePorte()
            .isBlank() ? logement.getNumeroDePorte()
            : logementUpdateDto.getNumeroDePorte());
    logement.setTypeLogement(
        logementUpdateDto.getIdTypeLogement() == null || logementUpdateDto.getIdTypeLogement() == 0
            ? logement.getTypeLogement()
            : typeLogementRepository.findById(logementUpdateDto.getIdTypeLogement()).orElseThrow(
                LogementInvalidTypeLogement::new));

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
