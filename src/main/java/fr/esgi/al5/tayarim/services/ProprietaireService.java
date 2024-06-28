package fr.esgi.al5.tayarim.services;

import fr.esgi.al5.tayarim.TayarimApplication;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireCandidateDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireCreationDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireUpdateDto;
import fr.esgi.al5.tayarim.entities.Amenagement;
import fr.esgi.al5.tayarim.entities.ImageLogement;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.entities.ReglesLogement;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.entities.TypeLogement;
import fr.esgi.al5.tayarim.exceptions.LogementImageBucketUploadError;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidAmenagement;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidReglesLogement;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidTypeLogement;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.PasswordHashNotPossibleException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireEmailAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireInvalidCandidatureBody;
import fr.esgi.al5.tayarim.exceptions.ProprietaireInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNumTelAlreadyExistException;
import fr.esgi.al5.tayarim.mappers.LogementMapper;
import fr.esgi.al5.tayarim.mappers.ProprietaireMapper;
import fr.esgi.al5.tayarim.repositories.AmenagementRepository;
import fr.esgi.al5.tayarim.repositories.ImageLogementRepository;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import fr.esgi.al5.tayarim.repositories.ReglesLogementRepository;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
import fr.esgi.al5.tayarim.repositories.TypeLogementRepository;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


/**
 * Service pour la gestion des propriétaires dans le système. Fournit des méthodes pour créer,
 * récupérer, mettre à jour et supprimer des propriétaires.
 */
@Service
@Transactional(readOnly = true)
public class ProprietaireService {

  private final ProprietaireRepository proprietaireRepository;
  private final LogementRepository logementRepository;
  private final TypeLogementRepository typeLogementRepository;
  private final ReglesLogementRepository reglesLogementRepository;
  private final AmenagementRepository amenagementRepository;
  private final ImageLogementRepository imageLogementRepository;
  private final ReservationRepository reservationRepository;

  /**
   * Constructeur pour le service de gestion des propriétaires.
   */
  public ProprietaireService(ProprietaireRepository proprietaireRepository,
      LogementRepository logementRepository, TypeLogementRepository typeLogementRepository,
      ReglesLogementRepository reglesLogementRepository,
      AmenagementRepository amenagementRepository,
      ImageLogementRepository imageLogementRepository,
      ReservationRepository reservationRepository) {
    this.proprietaireRepository = proprietaireRepository;
    this.logementRepository = logementRepository;
    this.typeLogementRepository = typeLogementRepository;
    this.reglesLogementRepository = reglesLogementRepository;
    this.amenagementRepository = amenagementRepository;
    this.imageLogementRepository = imageLogementRepository;
    this.reservationRepository = reservationRepository;
  }

  /**
   * Crée un nouveau propriétaire dans le système. Vérifie si l'email ou le numéro de téléphone
   * existe déjà avant de procéder à la création.
   *
   * @param proprietaireCreationDto Les données nécessaires pour créer un propriétaire.
   * @return Le DTO du propriétaire créé.
   * @throws ProprietaireEmailAlreadyExistException  si l'email existe déjà.
   * @throws ProprietaireNumTelAlreadyExistException si le numéro de téléphone existe déjà.
   */
  @Transactional
  public ProprietaireDto creerProprietaire(
      @NonNull ProprietaireCreationDto proprietaireCreationDto) {
    if (proprietaireRepository.findFirstByEmail(proprietaireCreationDto.getEmail()).isPresent()) {
      throw new ProprietaireEmailAlreadyExistException();
    }

    String numTel = proprietaireCreationDto.getNumTel();
    numTel = numTel.replaceAll(" ", "");
    if (proprietaireRepository.findFirstByNumTel(numTel).isPresent()) {
      throw new ProprietaireNumTelAlreadyExistException();
    }
    proprietaireCreationDto.setNumTel(numTel);

    String generatedPassword = hashPassword(generatePassword());

    System.out.println(generatedPassword); // waiting for SMTP

    Proprietaire proprietaire = ProprietaireMapper.creationDtoToEntity(proprietaireCreationDto,
        hashPassword(generatedPassword.toString()));
    return ProprietaireMapper.entityToDto(proprietaireRepository.save(proprietaire), false);
  }

  /**
   * Récupère une liste de tous les propriétaires.
   *
   * @param isLogement Indique si les logements associés doivent être inclus.
   * @return Liste de DTOs des propriétaires.
   */
  public List<ProprietaireDto> getProprietaire(boolean isLogement) {
    return ProprietaireMapper.entityListToDtoList(proprietaireRepository.findAll(), isLogement);
  }

  /**
   * Récupère un propriétaire par son identifiant, avec la possibilité d'inclure les logements
   * associés.
   *
   * @param id         L'identifiant du propriétaire à récupérer.
   * @param isLogement Indique si les logements associés doivent être inclus.
   * @return Le DTO du propriétaire.
   * @throws ProprietaireNotFoundException si le propriétaire n'est pas trouvé.
   */
  public ProprietaireDto getProprietaireById(@NonNull Long id, boolean isLogement) {

    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(id);
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }
    return ProprietaireMapper.entityToDto(optionalProprietaire.get(), isLogement);
  }

  /**
   * Récupère un propriétaire par son email.
   *
   * @param email L'email du propriétaire à récupérer.
   * @return Le DTO du propriétaire.
   * @throws ProprietaireNotFoundException si le propriétaire n'est pas trouvé.
   */
  public ProprietaireDto getProprietaireByEmail(@NonNull String email) {
    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findFirstByEmail(email);
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    return ProprietaireMapper.entityToDto(optionalProprietaire.get(), false);
  }

  /**
   * Met à jour les informations d'un propriétaire existant.
   *
   * @param id                    L'identifiant du propriétaire à mettre à jour.
   * @param proprietaireUpdateDto Les nouvelles informations du propriétaire.
   * @return Le DTO du propriétaire mis à jour.
   * @throws ProprietaireInvalidUpdateBody si les données de mise à jour sont invalides.
   */
  @Transactional
  public ProprietaireDto updateProprietaire(@NonNull Long id,
      @NonNull ProprietaireUpdateDto proprietaireUpdateDto) {

    if (
        (proprietaireUpdateDto.getNom() == null || proprietaireUpdateDto.getNom().isBlank())
            && (proprietaireUpdateDto.getPrenom() == null || proprietaireUpdateDto.getPrenom()
            .isBlank())
            && (proprietaireUpdateDto.getEmail() == null || proprietaireUpdateDto.getEmail()
            .isBlank())
            && (proprietaireUpdateDto.getAdresse() == null || proprietaireUpdateDto.getAdresse()
            .isBlank())
            && (proprietaireUpdateDto.getNumTel() == null || proprietaireUpdateDto.getNumTel()
            .isBlank())
            && (proprietaireUpdateDto.getMotDePasse() == null
            || proprietaireUpdateDto.getMotDePasse().isBlank())
            && (proprietaireUpdateDto.getCommission() == null
            || proprietaireUpdateDto.getCommission().isNaN())

    ) {
      throw new ProprietaireInvalidUpdateBody();
    }

    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(id);
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    if ((proprietaireUpdateDto.getEmail() != null && !proprietaireUpdateDto.getEmail().isBlank())
        && proprietaireRepository.findFirstByEmail(proprietaireUpdateDto.getEmail()).isPresent()) {
      throw new ProprietaireInvalidUpdateBody();
    }

    if ((proprietaireUpdateDto.getNumTel() != null && !proprietaireUpdateDto.getNumTel().isBlank())
        && proprietaireRepository.findFirstByNumTel(proprietaireUpdateDto.getNumTel())
        .isPresent()) {
      throw new ProprietaireInvalidUpdateBody();
    }

    Proprietaire proprietaire = optionalProprietaire.get();

    proprietaire.setNom(
        (proprietaireUpdateDto.getNom() != null && !proprietaireUpdateDto.getNom().isBlank())
            ? proprietaireUpdateDto.getNom() : proprietaire.getNom());
    proprietaire.setPrenom(
        (proprietaireUpdateDto.getPrenom() != null && !proprietaireUpdateDto.getPrenom().isBlank())
            ? proprietaireUpdateDto.getPrenom() : proprietaire.getPrenom());
    proprietaire.setEmail(
        (proprietaireUpdateDto.getEmail() != null && !proprietaireUpdateDto.getEmail().isBlank())
            ? proprietaireUpdateDto.getEmail() : proprietaire.getEmail());
    proprietaire.setNumTel(
        (proprietaireUpdateDto.getNumTel() != null && !proprietaireUpdateDto.getNumTel().isBlank())
            ? proprietaireUpdateDto.getNumTel() : proprietaire.getNumTel());
    proprietaire.setMotDePasse(
        (proprietaireUpdateDto.getMotDePasse() != null && !proprietaireUpdateDto.getMotDePasse()
            .isBlank()) ? hashPassword(proprietaireUpdateDto.getMotDePasse())
            : proprietaire.getMotDePasse());
    proprietaire.setIsPasswordUpdated(
        proprietaireUpdateDto.getMotDePasse() != null && !proprietaireUpdateDto.getMotDePasse()
            .isBlank() || proprietaire.getIsPasswordUpdated());
    proprietaire.setCommission(
        (proprietaireUpdateDto.getCommission() != null && !proprietaireUpdateDto.getCommission()
            .isNaN())
            ? proprietaireUpdateDto.getCommission() : proprietaire.getCommission());
    proprietaire.setAdresse(
        (proprietaireUpdateDto.getAdresse() != null && !proprietaireUpdateDto.getAdresse()
            .isBlank())
            ? proprietaireUpdateDto.getAdresse() : proprietaire.getAdresse());

    return ProprietaireMapper.entityToDto(proprietaireRepository.save(proprietaire), false);
  }

  /**
   * Supprime un propriétaire par son identifiant.
   *
   * @param id L'identifiant du propriétaire à supprimer.
   * @return Le DTO du propriétaire supprimé.
   * @throws ProprietaireNotFoundException si le propriétaire à supprimer n'est pas trouvé.
   */
  @Transactional
  public ProprietaireDto deleteProprietaire(@NonNull Long id) {

    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(id);
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    ProprietaireDto proprietaireDto = ProprietaireMapper.entityToDto(optionalProprietaire.get(),
        false);

    for (Logement logement : logementRepository.findAllByProprietaire(optionalProprietaire.get())) {

      for (Reservation reservation : reservationRepository.findAllByLogementId(logement.getId())) {
        reservationRepository.deleteById(reservation.getId());
      }

      logementRepository.delete(logement);
    }

    proprietaireRepository.deleteById(
        id); //voir si supprime aussi les logements dans la table logement

    return proprietaireDto;

  }

  /**
   * Enregistre la candidature d'un propriétaire.
   */
  @Transactional
  public ProprietaireDto candidate(@NonNull ProprietaireCandidateDto proprietaireCandidateDto) {

    if (
        verifyFieldCandidate(proprietaireCandidateDto)
    ) {
      throw new ProprietaireInvalidCandidatureBody();
    }

    if (proprietaireRepository.findFirstByEmail(proprietaireCandidateDto.getEmail()).isPresent()) {
      throw new ProprietaireEmailAlreadyExistException();
    }

    if (proprietaireRepository.findFirstByNumTel(proprietaireCandidateDto.getNumTel())
        .isPresent()) {
      throw new ProprietaireNumTelAlreadyExistException();
    }

    Proprietaire proprietaire = ProprietaireMapper.candidatureDtoToEntity(proprietaireCandidateDto,
        hashPassword(generatePassword()));

    //send the password when validated

    Optional<TypeLogement> optionalTypeLogement = typeLogementRepository
        .findById(proprietaireCandidateDto.getIdTypeLogement());
    if (optionalTypeLogement.isEmpty()) {
      throw new LogementInvalidTypeLogement();
    }
    TypeLogement typeLogement = optionalTypeLogement.get();

    ArrayList<ReglesLogement> reglesLogements = parseRegle(
        proprietaireCandidateDto.getReglesLogement());

    ArrayList<Amenagement> amenagements = parseAmenagement(
        proprietaireCandidateDto.getAmenagements());

    Logement logement = LogementMapper.candidatureDtoToEntity(proprietaireCandidateDto,
        proprietaire, typeLogement, reglesLogements, amenagements, new ArrayList<>());

    proprietaire = proprietaireRepository.save(proprietaire);

    proprietaire.setLogements(List.of(
        logementRepository.save(logement))
    );

    if (proprietaireCandidateDto.getFiles() != null) {
      int cpt = 0;
      List<String> urls = new ArrayList<>();
      ArrayList<ImageLogement> images = new ArrayList<>();
      for (MultipartFile file : proprietaireCandidateDto.getFiles()) {
        cpt++;

        try {
          // Obtenez les octets du fichier
          byte[] bytes = file.getBytes();

          String fileName = "House images/".concat(
                  proprietaire.getLogements().get(0).getId().toString()).concat("_")
              .concat(Integer.toString(cpt));

          // Téléchargez le fichier dans GCS
          TayarimApplication.bucket.create(fileName, bytes);

          urls.add(fileName);

        } catch (IOException e) {
          logementRepository.delete(logement);
          proprietaireRepository.delete(proprietaire);
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

    logementRepository.save(logement);

    return ProprietaireMapper.entityToDto(proprietaire, false);
  }

  /**
   * Valide un propriétaire candidat.
   */
  @Transactional
  public ProprietaireDto validateCandidat(@NonNull Long id) {
    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(id);
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    Proprietaire proprietaire = optionalProprietaire.get();

    if (proprietaire.getIsValidated()) {
      throw new ProprietaireNotFoundException();
    }

    proprietaire.setIsValidated(true);

    if (proprietaire.getLogements().get(0) == null) {
      throw new LogementNotFoundException();
    }

    Logement logement = proprietaire.getLogements().get(0);

    if (
        logement.getCapaciteMaxPersonne() == null
            || logement.getNombresNuitsMin() == null
            || logement.getPrixParNuit() == null
            || logement.getDefaultCheckIn() == null
            || logement.getDefaultCheckOut() == null
            || logement.getIntervalReservation() == null
    ) {
      throw new LogementInvalidUpdateBody();
    }

    logement.setIsValidated(true);
    logement = logementRepository.save(logement);

    System.out.println(logement.getIsValidated());

    return ProprietaireMapper.entityToDto(proprietaireRepository.save(proprietaire), false);
  }

  /**
   * Rejette un propriétaire candidat.
   */
  @Transactional
  public ProprietaireDto rejectCandidat(@NonNull Long id) {
    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(id);
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    Proprietaire proprietaire = optionalProprietaire.get();

    if (proprietaire.getIsValidated()) {
      throw new ProprietaireNotFoundException();
    }

    Logement logement = proprietaire.getLogements().get(0);

    logementRepository.delete(logement);
    proprietaireRepository.delete(proprietaire);

    return ProprietaireMapper.entityToDto(proprietaire, false);
  }

  /**
   * Vérifie si le mot de passe fourni correspond au mot de passe enregistré du propriétaire.
   *
   * @param password       Le mot de passe à vérifier.
   * @param proprietaireId L'identifiant du propriétaire.
   * @return true si le mot de passe correspond, false sinon.
   */
  public boolean verifyPassword(@NonNull String password, @NonNull Long proprietaireId) {
    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(proprietaireId);
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    Proprietaire proprietaire = optionalProprietaire.get();

    return verifyHashedPassword(password, proprietaire.getMotDePasse());

  }

  private String hashPassword(@NonNull String password) {
    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

    if (!verifyHashedPassword(password, hashedPassword)) {
      throw new PasswordHashNotPossibleException();
    }

    return hashedPassword;
  }

  private boolean verifyHashedPassword(@NonNull String password, @NonNull String hashedPassword) {
    return BCrypt.checkpw(password, hashedPassword);

  }

  private String generatePassword() {
    String allowedchar = "abcdefghijklmnopqrstuvwxyz"
        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "0123456789"
        + "!@#$%&*()_+-=[]?";
    SecureRandom random = new SecureRandom();
    StringBuilder generatedPassword = null;
    boolean result = false;
    while (!result) {
      boolean hasLowerCase = false;
      boolean hasUpperCase = false;
      boolean hasDigit = false;
      boolean hasSpecialChar = false;
      generatedPassword = new StringBuilder(16);
      for (int i = 0; i < 16; i++) {
        int index = random.nextInt(allowedchar.length());
        if (index > 61) {
          hasSpecialChar = true;
        } else if (index > 51) {
          hasDigit = true;
        } else if (index > 25) {
          hasUpperCase = true;
        } else {
          hasLowerCase = true;
        }
        generatedPassword.append(allowedchar.charAt(index));
      }
      result = hasLowerCase && hasUpperCase && hasDigit && hasSpecialChar;
    }
    return generatedPassword.toString();
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

  private boolean verifyFieldCandidate(@NonNull ProprietaireCandidateDto proprietaireCandidateDto) {
    return
        (
            proprietaireCandidateDto.getIsLouable()
                && (
                proprietaireCandidateDto.getNombresDeChambres() == null
                    || proprietaireCandidateDto.getNombresDeLits() == null
                    || proprietaireCandidateDto.getNombresSallesDeBains() == null
                    || proprietaireCandidateDto.getCapaciteMaxPersonne() == null
                    || proprietaireCandidateDto.getReglesLogement() == null
                    || proprietaireCandidateDto.getAmenagements() == null
                    || proprietaireCandidateDto.getFiles() == null
            )
        );
  }

}