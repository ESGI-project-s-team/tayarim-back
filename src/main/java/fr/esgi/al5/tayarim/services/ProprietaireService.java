package fr.esgi.al5.tayarim.services;

import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireCreationDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireUpdateDto;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.exceptions.PasswordHashNotPossibleException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireEmailAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNumTelAlreadyExistException;
import fr.esgi.al5.tayarim.mappers.ProprietaireMapper;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import java.security.SecureRandom;
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

  public ProprietaireService(ProprietaireRepository proprietaireRepository) {
    this.proprietaireRepository = proprietaireRepository;
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

    StringBuilder generatedPassword = new StringBuilder(16);
    String allowedchar = "abcdefghijklmnopqrstuvwxyz0123456789!@#$%&*()_+-=[]?";
    SecureRandom random = new SecureRandom();
    for (int i = 0; i < 16; i++) {
      int index = random.nextInt(allowedchar.length());
      generatedPassword.append(allowedchar.charAt(index));
    }

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
   * @param email      L'email du propriétaire à récupérer.
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
            && (proprietaireUpdateDto.getNumTel() == null || proprietaireUpdateDto.getNumTel()
            .isBlank())
            && (proprietaireUpdateDto.getMotDePasse() == null
            || proprietaireUpdateDto.getMotDePasse().isBlank())
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

    proprietaireRepository.deleteById(
        id); //voir si supprime aussi les logements dans la table logement

    return proprietaireDto;

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
}