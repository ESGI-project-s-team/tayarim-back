package fr.esgi.al5.tayarim.services;

import fr.esgi.al5.tayarim.dto.proprietaire.AdministrateurCreationDto;
import fr.esgi.al5.tayarim.dto.proprietaire.AdministrateurDto;
import fr.esgi.al5.tayarim.dto.proprietaire.AdministrateurUpdateDto;
import fr.esgi.al5.tayarim.entities.Administrateur;
import fr.esgi.al5.tayarim.exceptions.AdministrateurEmailAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.AdministrateurInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.AdministrateurNotFoundException;
import fr.esgi.al5.tayarim.exceptions.AdministrateurNumTelAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.PasswordHashNotPossibleException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireEmailAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNumTelAlreadyExistException;
import fr.esgi.al5.tayarim.mappers.AdministrateurMapper;
import fr.esgi.al5.tayarim.repositories.AdministrateurRepository;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service pour la gestion des administrateurs dans le système. Fournit des méthodes pour créer,
 * récupérer, mettre à jour et supprimer des administrateurs.
 */
@Service
@Transactional(readOnly = true)
public class AdministrateurService {

  private final AdministrateurRepository administrateurRepository;
  private final ProprietaireService proprietaireService;

  public AdministrateurService(AdministrateurRepository administrateurRepository,
      @Lazy ProprietaireService proprietaireService) {
    this.administrateurRepository = administrateurRepository;
    this.proprietaireService = proprietaireService;
  }

  /**
   * Crée un nouvel administrateur dans le système. Vérifie si l'email ou le numéro de téléphone
   * existe déjà avant de procéder à la création.
   *
   * @param administrateurCreationDto Les données nécessaires pour créer un administrateur.
   * @return Le DTO de l'administrateur créé.
   * @throws AdministrateurEmailAlreadyExistException  si l'email existe déjà.
   * @throws AdministrateurNumTelAlreadyExistException si le numéro de téléphone existe déjà.
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public AdministrateurDto creerAdministrateur(
      @NonNull AdministrateurCreationDto administrateurCreationDto) {
    if (administrateurRepository.findFirstByEmail(administrateurCreationDto.getEmail())
        .isPresent()) {
      throw new AdministrateurEmailAlreadyExistException();
    }

    try {
      proprietaireService.getProprietaireByEmail(administrateurCreationDto.getEmail());
      throw new AdministrateurEmailAlreadyExistException();
    } catch (AdministrateurEmailAlreadyExistException e) {
      throw e;
    } catch (Exception ignored) {
      // ignored ProprietaireNotFoundException
    }

    String numTel = administrateurCreationDto.getNumTel();
    numTel = numTel.replaceAll(" ", "");
    if (administrateurRepository.findFirstByNumTel(numTel).isPresent()) {
      throw new AdministrateurNumTelAlreadyExistException();
    }

    try {
      proprietaireService.getProprietaireByNumTel(administrateurCreationDto.getNumTel());
      throw new AdministrateurNumTelAlreadyExistException();
    } catch (AdministrateurNumTelAlreadyExistException e) {
      throw e;
    } catch (Exception ignored) {
      // ignored ProprietaireNotFoundException
    }
    administrateurCreationDto.setNumTel(numTel);

    String hashedPassword = hashPassword(administrateurCreationDto.getMotDePasse());

    administrateurCreationDto.setMotDePasse(hashedPassword);

    Administrateur administrateur = AdministrateurMapper.creationDtoToEntity(
        administrateurCreationDto);

    return AdministrateurMapper.entityToDto(administrateurRepository.save(administrateur));
  }

  /**
   * Récupère une liste de tous les administrateurs.
   *
   * @return Liste de DTOs des administrateurs.
   */
  public List<AdministrateurDto> getAdministrateur() {
    return AdministrateurMapper.entityListToDtoList(administrateurRepository.findAll());
  }

  /**
   * Récupère un administrateur par son identifiant.
   *
   * @param id L'identifiant de l'administrateur à récupérer.
   * @return Le DTO de l'administrateur.
   * @throws AdministrateurNotFoundException si l'administrateur n'est pas trouvé.
   */
  public AdministrateurDto getAdministrateurById(@NonNull Long id) {

    Optional<Administrateur> optionalAdministrateur = administrateurRepository.findById(id);
    if (optionalAdministrateur.isEmpty()) {
      throw new AdministrateurNotFoundException();
    }
    return AdministrateurMapper.entityToDto(optionalAdministrateur.get());
  }

  /**
   * Récupère un administrateur par son email.
   *
   * @param email L'email de l'administrateur à récupérer.
   * @return Le DTO de l'administrateur.
   * @throws AdministrateurNotFoundException si l'administrateur n'est pas trouvé.
   */
  public AdministrateurDto getAdministrateurByEmail(@NonNull String email) {
    Optional<Administrateur> optionalAdministrateur = administrateurRepository.findFirstByEmail(
        email);
    if (optionalAdministrateur.isEmpty()) {
      throw new AdministrateurNotFoundException();
    }

    return AdministrateurMapper.entityToDto(optionalAdministrateur.get());
  }

  /**
   * Récupère un administrateur par son numéro de téléphone.
   *
   * @param numTel Le numéro de l'administrateur à récupérer.
   * @return Le DTO de l'administrateur.
   * @throws AdministrateurNotFoundException si l'administrateur n'est pas trouvé.
   */
  public AdministrateurDto getAdministrateurByNumTel(@NonNull String numTel) {
    Optional<Administrateur> optionalAdministrateur = administrateurRepository.findFirstByNumTel(
        numTel);
    if (optionalAdministrateur.isEmpty()) {
      throw new AdministrateurNotFoundException();
    }

    return AdministrateurMapper.entityToDto(optionalAdministrateur.get());
  }

  /**
   * Met à jour les informations d'un administrateur existant.
   *
   * @param id                      L'identifiant de l'administrateur à mettre à jour.
   * @param administrateurUpdateDto Les nouvelles informations de l'administrateur.
   * @return Le DTO de l'administrateur mis à jour.
   * @throws AdministrateurInvalidUpdateBody si les données de mise à jour sont invalides.
   */
  @Transactional
  public AdministrateurDto updateAdministrateur(@NonNull Long id,
      @NonNull AdministrateurUpdateDto administrateurUpdateDto) {

    if (
        administrateurUpdateDto.getNom() == null
            && administrateurUpdateDto.getPrenom() == null
            && administrateurUpdateDto.getEmail() == null
            && administrateurUpdateDto.getNumTel() == null
            && administrateurUpdateDto.getMotDePasse() == null
    ) {
      throw new AdministrateurInvalidUpdateBody();
    }

    Optional<Administrateur> optionalAdministrateur = administrateurRepository.findById(id);
    if (optionalAdministrateur.isEmpty()) {
      throw new AdministrateurNotFoundException();
    }

    if (administrateurUpdateDto.getEmail() != null && administrateurRepository.findFirstByEmail(
        administrateurUpdateDto.getEmail()).isPresent()) {
      throw new ProprietaireInvalidUpdateBody();
    }

    if (administrateurUpdateDto.getNumTel() != null && administrateurRepository.findFirstByNumTel(
        administrateurUpdateDto.getNumTel()).isPresent()) {
      throw new ProprietaireInvalidUpdateBody();
    }

    Administrateur administrateur = optionalAdministrateur.get();

    administrateur.setNom(
        administrateurUpdateDto.getNom() != null ? administrateurUpdateDto.getNom()
            : administrateur.getNom());
    administrateur.setPrenom(
        administrateurUpdateDto.getPrenom() != null ? administrateurUpdateDto.getPrenom()
            : administrateur.getPrenom());
    administrateur.setEmail(
        administrateurUpdateDto.getEmail() != null ? administrateurUpdateDto.getEmail()
            : administrateur.getEmail());
    administrateur.setNumTel(
        administrateurUpdateDto.getNumTel() != null ? administrateurUpdateDto.getNumTel()
            : administrateur.getNumTel());
    administrateur.setMotDePasse(administrateurUpdateDto.getMotDePasse() != null ? hashPassword(
        administrateurUpdateDto.getMotDePasse()) : administrateur.getMotDePasse());

    return AdministrateurMapper.entityToDto(administrateurRepository.save(administrateur));
  }

  /**
   * Supprime un administrateur par son identifiant.
   *
   * @param id L'identifiant de l'administrateur à supprimer.
   * @return Le DTO de l'administrateur supprimé.
   * @throws AdministrateurNotFoundException si l'administrateur à supprimer n'est pas trouvé.
   */
  @Transactional
  public AdministrateurDto deleteAdministrateur(@NonNull Long id) {

    Optional<Administrateur> optionalAdministrateur = administrateurRepository.findById(id);
    if (optionalAdministrateur.isEmpty()) {
      throw new AdministrateurNotFoundException();
    }

    AdministrateurDto administrateurDto = AdministrateurMapper.entityToDto(
        optionalAdministrateur.get());

    administrateurRepository.deleteById(
        id); //voir si supprime aussi les logements dans la table logement

    return administrateurDto;

  }

  /**
   * Vérifie si le mot de passe fourni correspond au mot de passe enregistré de l'administrateur.
   *
   * @param password       Le mot de passe à vérifier.
   * @param proprietaireId L'identifiant de l'administrateur.
   * @return true si le mot de passe correspond, false sinon.
   */
  public boolean verifyPassword(@NonNull String password, @NonNull Long proprietaireId) {
    Optional<Administrateur> optionalAdministrateur = administrateurRepository.findById(
        proprietaireId);
    if (optionalAdministrateur.isEmpty()) {
      throw new AdministrateurNotFoundException();
    }

    Administrateur administrateur = optionalAdministrateur.get();

    return verifyHashedPassword(password, administrateur.getMotDePasse());

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
