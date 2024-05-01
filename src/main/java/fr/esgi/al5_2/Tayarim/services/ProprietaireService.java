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

@Service
@Transactional(readOnly = true)
public class ProprietaireService {

  private final ProprietaireRepository proprietaireRepository;

  public ProprietaireService(ProprietaireRepository proprietaireRepository) {
    this.proprietaireRepository = proprietaireRepository;
  }

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

  public List<ProprietaireDTO> getProprietaire(boolean isLogement) {
    return ProprietaireMapper.entityListToDtoList(proprietaireRepository.findAll(), isLogement);
  }

  public ProprietaireDTO getProprietaireById(@NonNull Long id, boolean isLogement) {

    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(id);
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }
    return ProprietaireMapper.entityToDto(optionalProprietaire.get(), isLogement);
  }

  public ProprietaireDTO getProprietaireByEmail(@NonNull String email) {
    Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findFirstByEmail(email);
    if (optionalProprietaire.isEmpty()) {
      throw new ProprietaireNotFoundException();
    }

    return ProprietaireMapper.entityToDto(optionalProprietaire.get(), false);
  }

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