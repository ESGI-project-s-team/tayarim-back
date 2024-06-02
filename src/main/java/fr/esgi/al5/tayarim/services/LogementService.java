package fr.esgi.al5.tayarim.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.al5.tayarim.dto.logement.LogementCreationDto;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.dto.logement.LogementUpdateDto;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.exceptions.LogementAddressCreationError;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.mappers.LogementMapper;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


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
   * @param logementRepository     Le repository des logements.
   * @param proprietaireRepository Le repository des propriétaires.
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

    String address =
        logementCreationDto.getAdresse() + ", " + logementCreationDto.getCodePostal() + " "
            + logementCreationDto.getVille() + ", " + logementCreationDto.getPays();

    //List<Double> coordinate = callOsm(address);
    List<Double> coordinate = callGeo(address);

    return LogementMapper.entityToDto(
        logementRepository.save(
            LogementMapper.creationDtoToEntity(
                logementCreationDto,
                coordinate.get(0),
                coordinate.get(1),
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
    double latitude = logement.getLatitude();
    double longitude = logement.getLongitude();

    logement.setTitre(
        (logementUpdateDto.getTitre() == null || logementUpdateDto.getTitre().isBlank())
            ? logement.getTitre()
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

    if ((logementUpdateDto.getAdresse() != null && !logementUpdateDto.getAdresse().isBlank())
        || (logementUpdateDto.getVille() != null && !logementUpdateDto.getVille().isBlank())
        || (logementUpdateDto.getCodePostal() != null && !logementUpdateDto.getCodePostal()
        .isBlank())
        || (logementUpdateDto.getPays() != null && !logementUpdateDto.getPays().isBlank())) {
      System.out.println(
          "do geocoding, address: " + logement.getAdresse() + ", " + logement.getVille() + ", "
              + logement.getCodePostal() + ", " + logement.getPays());
      List<Double> coordinate = callGeo(
          logement.getAdresse() + ", " + logement.getVille() + ", " + logement.getCodePostal()
              + ", " + logement.getPays());
      latitude = coordinate.get(0);
      longitude = coordinate.get(1);
    }

    logement.setLatitude(latitude);
    logement.setLongitude(longitude);
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

  /**
   * Réalise un appel à l'API OSM pour obtenir les coordonnées géographiques d'une adresse.
   *
   * @param address Adresse à géocoder
   * @return Liste contenant la latitude et la longitude de l'adresse
   */
  private List<Double> callOsm(String address) {
    double lat = -1;
    double lon = -1;
    try {
      String url = "https://nominatim.openstreetmap.org/search?q=" + address
          + "&format=json&addressdetails=1&limit=1";
      String response = new RestTemplate().getForObject(url, String.class);

      JsonNode root = new ObjectMapper().readTree(response);
      if (root.isArray() && root.size() > 0) {
        JsonNode location = root.get(0);
        lat = location.path("lat").asDouble();
        lon = location.path("lon").asDouble();
      } else {
        throw new LogementAddressCreationError();
      }
    } catch (LogementAddressCreationError | JsonProcessingException e) {
      throw new LogementAddressCreationError();
    }
    return List.of(lat, lon);
  }

  /**
   * Réalise un appel à l'API OSM pour obtenir les coordonnées géographiques d'une adresse.
   *
   * @param address Adresse à géocoder
   * @return Liste contenant la latitude et la longitude de l'adresse
   */
  private List<Double> callGeo(String address) {
    double lat = -1;
    double lon = -1;
    try {
      String url = "https://geocode.xyz/" + address + "?json=1&auth=60458438564472723419x76535";
      String response = new RestTemplate().getForObject(url, String.class);

      JsonNode root = new ObjectMapper().readTree(response);
      if (root.has("latt") && root.has("longt")) {
        lat = root.path("latt").asDouble();
        lon = root.path("longt").asDouble();
      } else {
        throw new LogementAddressCreationError();
      }
    } catch (LogementAddressCreationError | JsonProcessingException e) {
      throw new LogementAddressCreationError();
    }
    return List.of(lat, lon);
  }
}
