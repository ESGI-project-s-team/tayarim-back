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

@Service
@Transactional(readOnly = true)
public class AdministrateurService {

  private final AdministrateurRepository administrateurRepository;

  public AdministrateurService(AdministrateurRepository administrateurRepository) {
    this.administrateurRepository = administrateurRepository;
  }

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

  public List<AdministrateurDTO> getAdministrateur() {
    return AdministrateurMapper.entityListToDtoList(administrateurRepository.findAll());
  }

  public AdministrateurDTO getAdministrateurById(@NonNull Long id) {

    Optional<Administrateur> optionalAdministrateur = administrateurRepository.findById(id);
    if (optionalAdministrateur.isEmpty()) {
      throw new AdministrateurNotFoundException();
    }
    return AdministrateurMapper.entityToDto(optionalAdministrateur.get());
  }

  public AdministrateurDTO getAdministrateurByEmail(@NonNull String email) {
    Optional<Administrateur> optionalAdministrateur = administrateurRepository.findFirstByEmail(
        email);
    if (optionalAdministrateur.isEmpty()) {
      throw new AdministrateurNotFoundException();
    }

    return AdministrateurMapper.entityToDto(optionalAdministrateur.get());
  }

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
