package fr.esgi.al5.tayarim.services;


import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import fr.esgi.al5.tayarim.TayarimApplication;
import fr.esgi.al5.tayarim.dto.logement.LogementCreationDto;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.dto.logement.LogementSearchDto;
import fr.esgi.al5.tayarim.dto.logement.LogementUpdateDto;
import fr.esgi.al5.tayarim.dto.logement.TypeLogementDto;
import fr.esgi.al5.tayarim.entities.Amenagement;
import fr.esgi.al5.tayarim.entities.ImageLogement;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.entities.ReglesLogement;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.entities.TypeLogement;
import fr.esgi.al5.tayarim.exceptions.LogementImageBucketUploadError;
import fr.esgi.al5.tayarim.exceptions.LogementImageInvalidError;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidAmenagement;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidCreationBody;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidReglesLogement;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidTypeLogement;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ReservationDateConflictError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateInvalideError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateTooShortError;
import fr.esgi.al5.tayarim.exceptions.SearchDateInvalidError;
import fr.esgi.al5.tayarim.exceptions.SearchDateMissingError;
import fr.esgi.al5.tayarim.mappers.ImageLogementMapper;
import fr.esgi.al5.tayarim.mappers.LogementMapper;
import fr.esgi.al5.tayarim.mappers.TypeLogementMapper;
import fr.esgi.al5.tayarim.repositories.AmenagementRepository;
import fr.esgi.al5.tayarim.repositories.ImageLogementRepository;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import fr.esgi.al5.tayarim.repositories.ReglesLogementRepository;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
import fr.esgi.al5.tayarim.repositories.TypeLogementRepository;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


/**
 * Classe de service gérant les logements.
 */
@Service
@Transactional(readOnly = true)
public class LogementService {

  private final LogementRepository logementRepository;
  private final ProprietaireRepository proprietaireRepository;
  private final TypeLogementRepository typeLogementRepository;

  private final ReglesLogementRepository reglesLogementRepository;
  private final AmenagementRepository amenagementRepository;

  private final ReservationService reservationService;

  private final ReservationRepository reservationRepository;

  private final ImageLogementRepository imageLogementRepository;


  /**
   * Constructeur pour le service de logement.
   *
   * @param logementRepository       Le repository des logements.
   * @param proprietaireRepository   Le repository des propriétaires.
   * @param typeLogementRepository   Le repository des types de logements.
   * @param reglesLogementRepository Le repository des règles de logements.
   * @param amenagementRepository    Le repository des aménagements.
   * @param reservationService       Le service des réservations.
   * @param reservationRepository    Le repository des réservations
   * @param imageLogementRepository  Le repository des images de logement
   */
  public LogementService(LogementRepository logementRepository,
      ProprietaireRepository proprietaireRepository,
      TypeLogementRepository typeLogementRepository,
      ReglesLogementRepository reglesLogementRepository,
      AmenagementRepository amenagementRepository, ReservationService reservationService,
      ReservationRepository reservationRepository,
      ImageLogementRepository imageLogementRepository) {
    this.logementRepository = logementRepository;
    this.proprietaireRepository = proprietaireRepository;
    this.typeLogementRepository = typeLogementRepository;
    this.reglesLogementRepository = reglesLogementRepository;
    this.amenagementRepository = amenagementRepository;
    this.reservationService = reservationService;
    this.reservationRepository = reservationRepository;
    this.imageLogementRepository = imageLogementRepository;
  }

  /**
   * Tente de créer un logement.
   *
   * @param logementCreationDto Le dto de création de logement.
   * @return {@link LogementDto}
   */
  @Transactional
  public LogementDto createLogement(@NonNull LogementCreationDto logementCreationDto) {

    if (logementCreationDto.getFiles() != null && logementCreationDto.getFiles().isEmpty()) {
      throw new LogementImageInvalidError();
    }

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
                || (logementCreationDto.getReglesLogement() == null
                || logementCreationDto.getReglesLogement().isEmpty())
                || (logementCreationDto.getAmenagements() == null
                || logementCreationDto.getAmenagements().isEmpty())
                || (logementCreationDto.getFiles() == null || logementCreationDto.getFiles()
                .isEmpty())
        )) {
      throw new LogementInvalidCreationBody();
    }

    Optional<TypeLogement> optionalTypeLogement = typeLogementRepository.findById(
        logementCreationDto.getIdTypeLogement());
    if (optionalTypeLogement.isEmpty()) {
      throw new LogementInvalidTypeLogement();
    }

    ArrayList<ReglesLogement> reglesLogements = parseRegle(logementCreationDto.getReglesLogement());

    ArrayList<Amenagement> amenagements = parseAmenagement(logementCreationDto.getAmenagements());

    Logement logement = logementRepository.save(
        LogementMapper.creationDtoToEntity(
            logementCreationDto,
            optionalTypeLogement.get(),
            proprietaire,
            new HashSet<>(reglesLogements),
            new HashSet<>(amenagements),
            List.of()
        )
    );

    if (logementCreationDto.getFiles() != null && !logementCreationDto.getFiles().isEmpty()) {
      int cpt = 0;
      List<String> urls = new ArrayList<>();
      ArrayList<ImageLogement> images = new ArrayList<>();
      for (MultipartFile file : logementCreationDto.getFiles()) {
        cpt++;

        try {
          // Obtenez les octets du fichier
          byte[] bytes = file.getBytes();

          String fileName = "House images/".concat(logement.getId().toString()).concat("_")
              .concat(Integer.toString(cpt));

          // Téléchargez le fichier dans GCS
          TayarimApplication.bucket.create(fileName, bytes);

          urls.add(fileName);


        } catch (IOException e) {
          e.printStackTrace();
          logementRepository.delete(logement);
          throw new LogementImageBucketUploadError();
        }

      }

      cpt = 0;
      for (String fileName : urls) {
        cpt++;
        images.add(imageLogementRepository.save(new ImageLogement(fileName, logement, (cpt == 1))));
      }

      logement.setImages(images);

      logementRepository.save(logement);
    }

    System.out.println("end");

    return LogementMapper.entityToDto(
        logement
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

    List<Logement> logements = logementRepository.findAllByProprietaire(optionalProprietaire.get());

    for (Logement logement : logements) {
      System.out.println(logement.getImages().size());
    }

    return LogementMapper.entityListToDtoList(
        logements
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
        && (logementUpdateDto.getIdProprietaire() == null
        || logementUpdateDto.getIdProprietaire() == 0)
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
        || logementUpdateDto.getIdTypeLogement() == 0)
        && (logementUpdateDto.getReglesLogement() == null || logementUpdateDto.getReglesLogement()
        .isEmpty())
        && (logementUpdateDto.getAmenagements() == null || logementUpdateDto.getAmenagements()
        .isEmpty())
        && (logementUpdateDto.getFiles() == null || logementUpdateDto.getFiles().isEmpty())
        && (logementUpdateDto.getCurrentImages() == null || logementUpdateDto.getCurrentImages()
        .isEmpty())
    ) {
      throw new LogementInvalidUpdateBody();
    }

    Optional<Logement> optionalLogement = logementRepository.findById(id);

    if (optionalLogement.isEmpty()) {
      throw new LogementNotFoundException();
    }

    Logement logement = optionalLogement.get();

    if (
        (!logement.getIsLouable() && (logementUpdateDto.getIsLouable() != null
            && logementUpdateDto.getIsLouable()))
            &&
            (
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
                    || (logement.getPrixParNuit() == null
                    && logementUpdateDto.getPrixParNuit() == null)
                    || (logement.getReglesLogements() == null
                    && (logementUpdateDto.getReglesLogement() == null
                    || logementUpdateDto.getReglesLogement().isEmpty()))
                    || (logement.getAmenagements() == null
                    && (logementUpdateDto.getAmenagements() == null
                    || logementUpdateDto.getAmenagements().isEmpty()))
                    || (logement.getImages() == null
                    && (logementUpdateDto.getFiles() == null
                    || logementUpdateDto.getFiles().isEmpty()))

            )
    ) {
      throw new LogementInvalidUpdateBody();
    }

    if (logementUpdateDto.getIdProprietaire() != null
        && logementUpdateDto.getIdProprietaire() != 0) {
      Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(
          logementUpdateDto.getIdProprietaire());
      if (optionalProprietaire.isEmpty()) {
        throw new ProprietaireNotFoundException();
      }
      logement.setProprietaire(optionalProprietaire.get());
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
    logement.setReglesLogements(
        logementUpdateDto.getReglesLogement() == null || logementUpdateDto.getReglesLogement()
            .isEmpty() ? logement.getReglesLogements()
            : new HashSet<>(parseRegle(logementUpdateDto.getReglesLogement())));
    logement.setAmenagements(
        logementUpdateDto.getAmenagements() == null || logementUpdateDto.getAmenagements().isEmpty()
            ? logement.getAmenagements()
            : new HashSet<>(parseAmenagement(logementUpdateDto.getAmenagements())));

    if (logementUpdateDto.getFiles() != null && !logementUpdateDto.getFiles().isEmpty()) {

      int cpt = 0;
      List<String> urls = new ArrayList<>();
      ArrayList<ImageLogement> images = new ArrayList<>();

      if (logementUpdateDto.getCurrentImages() == null || logementUpdateDto.getCurrentImages()
          .isEmpty()) {
        imageLogementRepository.deleteAllByLogementId(logement.getId());
      } else {
        List<ImageLogement> oldImages = logement.getImages();

        imageLogementRepository.deleteUnusedImage(
            logement.getId(),
            logementUpdateDto.getCurrentImages()
        );

        for (ImageLogement image : oldImages) {
          cpt++;
          if (logementUpdateDto.getCurrentImages().contains(image.getId())) {
            images.add(image);
          }
        }
      }

      for (MultipartFile file : logementUpdateDto.getFiles()) {
        cpt++;
        try {
          // Obtenez les octets du fichier
          byte[] bytes = file.getBytes();

          String fileName = "House images/".concat(logement.getId().toString()).concat("_")
              .concat(Integer.toString(cpt));

          // Téléchargez le fichier dans GCS
          TayarimApplication.bucket.create(fileName, bytes);

          urls.add(fileName);

        } catch (IOException e) {
          e.printStackTrace();
          throw new LogementImageBucketUploadError();
        }

      }

      cpt = 0;

      for (String fileName : urls) {
        cpt++;
        images.add(imageLogementRepository.save(new ImageLogement(fileName, logement, (cpt == 1))));
      }

      logement.setImages(images);
    }

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
   * Récupère tous les type de logements.
   *
   * @return Le DTO des type de logements.
   */
  @Transactional
  public List<TypeLogementDto> getAllTypeLogement() {
    return TypeLogementMapper.entityListToDtoList(
        typeLogementRepository.findAll()
    );
  }

  /**
   * Tnte de récupèrer des logements basé sur certain critère.
   *
   * @param logementSearchDto dto de recherche de logement
   */

  @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
  public List<LogementDto> search(LogementSearchDto logementSearchDto) {

    if ((logementSearchDto.getDateArrivee() != null && logementSearchDto.getDateDepart() == null)
        || (logementSearchDto.getDateArrivee() == null
        && logementSearchDto.getDateDepart() != null)) {
      throw new SearchDateMissingError();
    }

    List<Logement> logements = logementRepository.findAll();

    logements = logements.stream()
        .filter(Logement::getIsLouable)
        .collect(Collectors.toList());

    if (logementSearchDto.getDestination() != null && !logementSearchDto.getDestination()
        .isBlank()) {
      logements = logements.stream()
          .filter(logement -> logement.getAdresse().toUpperCase()
              .contains(logementSearchDto.getDestination().toUpperCase())
              || logement.getVille().toUpperCase()
              .contains(logementSearchDto.getDestination().toUpperCase())
              || logement.getPays().toUpperCase()
              .contains(logementSearchDto.getDestination().toUpperCase()))
          .collect(Collectors.toList());
    }

    if (logementSearchDto.getNbPersonnes() != null) {
      logements = logements.stream()
          .filter(
              logement -> logement.getCapaciteMaxPersonne() >= logementSearchDto.getNbPersonnes())
          .collect(Collectors.toList());
    }

    if (logementSearchDto.getDateArrivee() != null && logementSearchDto.getDateDepart() != null) {
      LocalDate dateArrivee = LocalDate.parse(logementSearchDto.getDateArrivee());
      LocalDate dateDepart = LocalDate.parse(logementSearchDto.getDateDepart());

      if (dateArrivee.isAfter(dateDepart)) {
        throw new SearchDateInvalidError();
      }

      logements = logements.stream().filter(logement -> {
        try {
          reservationService.checkDateCondition(
              dateArrivee,
              dateDepart,
              logement.getNombresNuitsMin(),
              false
          );
          reservationService.checkDateConclict(
              "RESA-SEARCHING",
              dateArrivee,
              dateDepart,
              logement.getId(),
              false
          );
          return true;
        } catch (ReservationDateInvalideError | ReservationDateConflictError
                 | ReservationDateTooShortError e) {
          return false;
        }
      }).collect(Collectors.toList());
    }

    return LogementMapper.entityListToDtoList(logements);
  }

  /**
   * Récupère les dates occupées d'un logement.
   *
   * @param id Id du logement.
   * @return Les dates occupées.
   */
  @Transactional
  public List<String> getOccupiedDates(@NonNull Long id) {
    Optional<Logement> optionalLogement = logementRepository.findById(id);
    if (optionalLogement.isEmpty()) {
      throw new LogementNotFoundException();
    }

    Logement logement = optionalLogement.get();

    List<Reservation> reservations = reservationRepository.findAllByLogementIdAndStatutIn(
        logement.getId(),
        List.of("payed", "in progress"));

    ArrayList<String> dates = new ArrayList<>();

    for (Reservation reservation : reservations) {
      LocalDate currentDate = reservation.getDateArrivee().minusDays(2);
      LocalDate dateDepart = reservation.getDateDepart().plusDays(2);

      while (!currentDate.isAfter(dateDepart)) {
        if (!dates.contains(currentDate.toString())) {
          dates.add(currentDate.toString());
        }
        currentDate = currentDate.plusDays(1);
      }
    }

    return dates.stream().toList();

  }

  private ArrayList<ReglesLogement> parseRegle(List<Long> idRegles) {
    if (idRegles == null) {
      return new ArrayList<>();
    }

    ArrayList<ReglesLogement> reglesLogements = new ArrayList<>();
    for (Long id : idRegles) {
      Optional<ReglesLogement> optionalReglesLogement = reglesLogementRepository.findById(id);
      if (optionalReglesLogement.isEmpty()) {
        throw new LogementInvalidReglesLogement();
      }
      reglesLogements.add(optionalReglesLogement.get());
    }

    return reglesLogements;

  }

  private ArrayList<Amenagement> parseAmenagement(List<Long> idAmenagements) {
    if (idAmenagements == null) {
      return new ArrayList<>();
    }

    ArrayList<Amenagement> amenagements = new ArrayList<>();
    for (Long id : idAmenagements) {
      Optional<Amenagement> optionalAmenagement = amenagementRepository.findById(id);
      if (optionalAmenagement.isEmpty()) {
        throw new LogementInvalidAmenagement();
      }
      amenagements.add(optionalAmenagement.get());
    }

    return amenagements;

  }
}
