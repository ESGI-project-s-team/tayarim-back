package fr.esgi.al5_2.Tayarim.services;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireUpdateDTO;
import fr.esgi.al5_2.Tayarim.entities.Utilisateur;
import fr.esgi.al5_2.Tayarim.exceptions.*;
import fr.esgi.al5_2.Tayarim.mappers.LogementMapper;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import fr.esgi.al5_2.Tayarim.mappers.ProprietaireMapper;
import fr.esgi.al5_2.Tayarim.repositories.ProprietaireRepository;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

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
   * @param proprietaireCreationDTO Les données nécessaires pour créer un propriétaire.
   * @return Le DTO du propriétaire créé.
   * @throws ProprietaireEmailAlreadyExistException  si l'email existe déjà.
   * @throws ProprietaireNumTelAlreadyExistException si le numéro de téléphone existe déjà.
   */
  @Transactional
  public ProprietaireDTO creerProprietaire(
      @NonNull ProprietaireCreationDTO proprietaireCreationDTO) {
    if (proprietaireRepository.findFirstByEmail(proprietaireCreationDTO.getEmail()).isPresent()) {
      throw new ProprietaireEmailAlreadyExistException();
    }

    String numTel = proprietaireCreationDTO.getNumTel();
    numTel = numTel.replaceAll(" ", "");
    if (proprietaireRepository.findFirstByNumTel(numTel).isPresent()) {
      throw new ProprietaireNumTelAlreadyExistException();
    }
    proprietaireCreationDTO.setNumTel(numTel);

    StringBuilder generatedPassword = new StringBuilder(16);
    String allowedchar = "abcdefghijklmnopqrstuvwxyz0123456789!@#$%&*()_+-=[]?";
    SecureRandom random = new SecureRandom();
    for (int i = 0; i < 16; i++) {
      int index = random.nextInt(allowedchar.length());
      generatedPassword.append(allowedchar.charAt(index));
    }

    System.out.println(generatedPassword); // waiting for SMTP

    Proprietaire proprietaire = ProprietaireMapper.creationDtoToEntity(proprietaireCreationDTO,
        hashPassword(generatedPassword.toString()));
    return ProprietaireMapper.entityToDto(proprietaireRepository.save(proprietaire), false);
  }

  /**
   * Récupère une liste de tous les propriétaires.
   *
   * @param isLogement Indique si les logements associés doivent être inclus.
   * @return Liste de DTOs des propriétaires.
   */
  public List<ProprietaireDTO> getProprietaire(boolean isLogement) {
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
  public ProprietaireDTO getProprietaireById(@NonNull Long id, boolean isLogement) {

    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(id);
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }
    return ProprietaireMapper.entityToDto(optionalProprietaire.get(), isLogement);
  }

  /**
   * Récupère un propriétaire par son email
   *
   * @param email      L'email du propriétaire à récupérer.
   * @return Le DTO du propriétaire.
   * @throws ProprietaireNotFoundException si le propriétaire n'est pas trouvé.
   */
  public ProprietaireDTO getProprietaireByEmail(@NonNull String email) {
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
   * @param proprietaireUpdateDTO Les nouvelles informations du propriétaire.
   * @return Le DTO du propriétaire mis à jour.
   * @throws ProprietaireInvalidUpdateBody si les données de mise à jour sont invalides.
   */
  @Transactional
  public ProprietaireDTO updateProprietaire(@NonNull Long id,
      @NonNull ProprietaireUpdateDTO proprietaireUpdateDTO) {

    if (
        (proprietaireUpdateDTO.getNom() == null || proprietaireUpdateDTO.getNom().isBlank())
            && (proprietaireUpdateDTO.getPrenom() == null || proprietaireUpdateDTO.getPrenom()
            .isBlank())
            && (proprietaireUpdateDTO.getEmail() == null || proprietaireUpdateDTO.getEmail()
            .isBlank())
            && (proprietaireUpdateDTO.getNumTel() == null || proprietaireUpdateDTO.getNumTel()
            .isBlank())
            && (proprietaireUpdateDTO.getMotDePasse() == null
            || proprietaireUpdateDTO.getMotDePasse().isBlank())
    ) {
      throw new ProprietaireInvalidUpdateBody();
    }

    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(id);
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    if ((proprietaireUpdateDTO.getEmail() != null && !proprietaireUpdateDTO.getEmail().isBlank())
        && proprietaireRepository.findFirstByEmail(proprietaireUpdateDTO.getEmail()).isPresent()) {
      throw new ProprietaireInvalidUpdateBody();
    }

    if ((proprietaireUpdateDTO.getNumTel() != null && !proprietaireUpdateDTO.getNumTel().isBlank())
        && proprietaireRepository.findFirstByNumTel(proprietaireUpdateDTO.getNumTel())
        .isPresent()) {
      throw new ProprietaireInvalidUpdateBody();
    }

    Proprietaire proprietaire = optionalProprietaire.get();

    proprietaire.setNom(
        (proprietaireUpdateDTO.getNom() != null && !proprietaireUpdateDTO.getNom().isBlank())
            ? proprietaireUpdateDTO.getNom() : proprietaire.getNom());
    proprietaire.setPrenom(
        (proprietaireUpdateDTO.getPrenom() != null && !proprietaireUpdateDTO.getPrenom().isBlank())
            ? proprietaireUpdateDTO.getPrenom() : proprietaire.getPrenom());
    proprietaire.setEmail(
        (proprietaireUpdateDTO.getEmail() != null && !proprietaireUpdateDTO.getEmail().isBlank())
            ? proprietaireUpdateDTO.getEmail() : proprietaire.getEmail());
    proprietaire.setNumTel(
        (proprietaireUpdateDTO.getNumTel() != null && !proprietaireUpdateDTO.getNumTel().isBlank())
            ? proprietaireUpdateDTO.getNumTel() : proprietaire.getNumTel());
    proprietaire.setMotDePasse(
        (proprietaireUpdateDTO.getMotDePasse() != null && !proprietaireUpdateDTO.getMotDePasse()
            .isBlank()) ? hashPassword(proprietaireUpdateDTO.getMotDePasse())
            : proprietaire.getMotDePasse());
    proprietaire.setIsPasswordUpdated(
        proprietaireUpdateDTO.getMotDePasse() != null && !proprietaireUpdateDTO.getMotDePasse()
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
  public ProprietaireDTO deleteProprietaire(@NonNull Long id) {

    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(id);
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    ProprietaireDTO proprietaireDTO = ProprietaireMapper.entityToDto(optionalProprietaire.get(),
        false);

    proprietaireRepository.deleteById(
        id); //voir si supprime aussi les logements dans la table logement

    return proprietaireDTO;

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