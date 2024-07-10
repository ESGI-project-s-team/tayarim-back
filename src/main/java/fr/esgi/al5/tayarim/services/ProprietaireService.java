package fr.esgi.al5.tayarim.services;

import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireCandidateDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireCreationDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireUpdateDto;
import fr.esgi.al5.tayarim.entities.Amenagement;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.entities.ReglesLogement;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidAmenagement;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidReglesLogement;
import fr.esgi.al5.tayarim.exceptions.PasswordHashNotPossibleException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireEmailAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNumTelAlreadyExistException;
import fr.esgi.al5.tayarim.mail.EmailService;
import fr.esgi.al5.tayarim.mappers.ProprietaireMapper;
import fr.esgi.al5.tayarim.repositories.AmenagementRepository;
import fr.esgi.al5.tayarim.repositories.ImageLogementRepository;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import fr.esgi.al5.tayarim.repositories.ReglesLogementRepository;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
import fr.esgi.al5.tayarim.repositories.TypeLogementRepository;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
  private final EmailService emailService;

  /**
   * Constructeur pour le service de gestion des propriétaires.
   */
  public ProprietaireService(ProprietaireRepository proprietaireRepository,
      LogementRepository logementRepository, TypeLogementRepository typeLogementRepository,
      ReglesLogementRepository reglesLogementRepository,
      AmenagementRepository amenagementRepository,
      ImageLogementRepository imageLogementRepository,
      ReservationRepository reservationRepository, EmailService emailService) {
    this.proprietaireRepository = proprietaireRepository;
    this.logementRepository = logementRepository;
    this.typeLogementRepository = typeLogementRepository;
    this.reglesLogementRepository = reglesLogementRepository;
    this.amenagementRepository = amenagementRepository;
    this.imageLogementRepository = imageLogementRepository;
    this.reservationRepository = reservationRepository;
    this.emailService = emailService;
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

    proprietaire = proprietaireRepository.save(proprietaire);

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

    String generatedPassword = generatePassword();
    proprietaire.setMotDePasse(hashPassword(generatedPassword));

    proprietaire = proprietaireRepository.save(proprietaire);

    emailService.sendAccountConfirmationEmail(proprietaire.getNom(), proprietaire.getPrenom(),
        proprietaire.getEmail(), generatedPassword);

    return ProprietaireMapper.entityToDto(proprietaire, false);
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
      System.out.println(generatedPassword.toString());
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

}