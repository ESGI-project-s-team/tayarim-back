package fr.esgi.al5_2.Tayarim.services;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.*;
import fr.esgi.al5_2.Tayarim.entities.Administrateur;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import fr.esgi.al5_2.Tayarim.exceptions.*;
import fr.esgi.al5_2.Tayarim.mappers.AdministrateurMapper;
import fr.esgi.al5_2.Tayarim.mappers.ProprietaireMapper;
import fr.esgi.al5_2.Tayarim.repositories.AdministrateurRepository;
import fr.esgi.al5_2.Tayarim.repositories.ProprietaireRepository;
import lombok.NonNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des administrateurs dans le système. Fournit des méthodes pour créer,
 * récupérer, mettre à jour et supprimer des administrateurs.
 */
@Service
@Transactional(readOnly = true)
public class AdministrateurService {

  private final AdministrateurRepository administrateurRepository;

  public AdministrateurService(AdministrateurRepository administrateurRepository) {
    this.administrateurRepository = administrateurRepository;
  }

  /**
   * Crée un nouvel administrateur dans le système. Vérifie si l'email ou le numéro de téléphone
   * existe déjà avant de procéder à la création.
   *
   * @param administrateurCreationDTO Les données nécessaires pour créer un administrateur.
   * @return Le DTO de l'administrateur créé.
   * @throws AdministrateurEmailAlreadyExistException  si l'email existe déjà.
   * @throws AdministrateurNumTelAlreadyExistException si le numéro de téléphone existe déjà.
   */
  @Transactional
  public AdministrateurDTO creerAdministrateur(
      @NonNull AdministrateurCreationDTO administrateurCreationDTO) {
    if (administrateurRepository.findFirstByEmail(administrateurCreationDTO.getEmail())
        .isPresent()) {
      throw new AdministrateurEmailAlreadyExistException();
    }

    String numTel = administrateurCreationDTO.getNumTel();
    numTel = numTel.replaceAll(" ", "");
    if (administrateurRepository.findFirstByNumTel(numTel).isPresent()) {
      throw new AdministrateurNumTelAlreadyExistException();
    }
    administrateurCreationDTO.setNumTel(numTel);

    String hashedPassword = hashPassword(administrateurCreationDTO.getMotDePasse());

    administrateurCreationDTO.setMotDePasse(hashedPassword);

    Administrateur administrateur = AdministrateurMapper.creationDtoToEntity(
        administrateurCreationDTO);

    return AdministrateurMapper.entityToDto(administrateurRepository.save(administrateur));
  }

  /**
   * Récupère une liste de tous les administrateurs.
   *
   * @return Liste de DTOs des administrateurs.
   */
  public List<AdministrateurDTO> getAdministrateur() {
    return AdministrateurMapper.entityListToDtoList(administrateurRepository.findAll());
  }

  /**
   * Récupère un administrateur par son identifiant.
   *
   * @param id L'identifiant de l'administrateur à récupérer.
   * @return Le DTO de l'administrateur.
   * @throws AdministrateurNotFoundException si l'administrateur n'est pas trouvé.
   */
  public AdministrateurDTO getAdministrateurById(@NonNull Long id) {

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
  public AdministrateurDTO getAdministrateurByEmail(@NonNull String email) {
    Optional<Administrateur> optionalAdministrateur = administrateurRepository.findFirstByEmail(
        email);
    if (optionalAdministrateur.isEmpty()) {
      throw new AdministrateurNotFoundException();
    }

    return AdministrateurMapper.entityToDto(optionalAdministrateur.get());
  }

  /**
   * Met à jour les informations d'un administrateur existant.
   *
   * @param id                      L'identifiant de l'administrateur à mettre à jour.
   * @param administrateurUpdateDTO Les nouvelles informations de l'administrateur.
   * @return Le DTO de l'administrateur mis à jour.
   * @throws AdministrateurInvalidUpdateBody si les données de mise à jour sont invalides.
   */
  @Transactional
  public AdministrateurDTO updateAdministrateur(@NonNull Long id,
      @NonNull AdministrateurUpdateDTO administrateurUpdateDTO) {

    if (
        administrateurUpdateDTO.getNom() == null
            && administrateurUpdateDTO.getPrenom() == null
            && administrateurUpdateDTO.getEmail() == null
            && administrateurUpdateDTO.getNumTel() == null
            && administrateurUpdateDTO.getMotDePasse() == null
    ) {
      throw new AdministrateurInvalidUpdateBody();
    }

    Optional<Administrateur> optionalAdministrateur = administrateurRepository.findById(id);
    if (optionalAdministrateur.isEmpty()) {
      throw new AdministrateurNotFoundException();
    }

    if (administrateurUpdateDTO.getEmail() != null && administrateurRepository.findFirstByEmail(
        administrateurUpdateDTO.getEmail()).isPresent()) {
      throw new ProprietaireInvalidUpdateBody();
    }

    if (administrateurUpdateDTO.getNumTel() != null && administrateurRepository.findFirstByNumTel(
        administrateurUpdateDTO.getNumTel()).isPresent()) {
      throw new ProprietaireInvalidUpdateBody();
    }

    Administrateur administrateur = optionalAdministrateur.get();

    administrateur.setNom(
        administrateurUpdateDTO.getNom() != null ? administrateurUpdateDTO.getNom()
            : administrateur.getNom());
    administrateur.setPrenom(
        administrateurUpdateDTO.getPrenom() != null ? administrateurUpdateDTO.getPrenom()
            : administrateur.getPrenom());
    administrateur.setEmail(
        administrateurUpdateDTO.getEmail() != null ? administrateurUpdateDTO.getEmail()
            : administrateur.getEmail());
    administrateur.setNumTel(
        administrateurUpdateDTO.getNumTel() != null ? administrateurUpdateDTO.getNumTel()
            : administrateur.getNumTel());
    administrateur.setMotDePasse(administrateurUpdateDTO.getMotDePasse() != null ? hashPassword(
        administrateurUpdateDTO.getMotDePasse()) : administrateur.getMotDePasse());

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
  public AdministrateurDTO deleteAdministrateur(@NonNull Long id) {

    Optional<Administrateur> optionalAdministrateur = administrateurRepository.findById(id);
    if (optionalAdministrateur.isEmpty()) {
      throw new AdministrateurNotFoundException();
    }

    AdministrateurDTO administrateurDTO = AdministrateurMapper.entityToDto(
        optionalAdministrateur.get());

    administrateurRepository.deleteById(
        id); //voir si supprime aussi les logements dans la table logement

    return administrateurDTO;

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
