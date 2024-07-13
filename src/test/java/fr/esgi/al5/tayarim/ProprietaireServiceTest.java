package fr.esgi.al5.tayarim;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireCreationDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireUpdateDto;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.mail.EmailService;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import fr.esgi.al5.tayarim.services.ProprietaireService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Classe de test du service Proprietaire.
 */
@ExtendWith(SpringExtension.class)
public class ProprietaireServiceTest {

  @Mock
  private ProprietaireRepository proprietaireRepository;

  @InjectMocks
  private ProprietaireService proprietaireService;

  @Mock
  private LogementRepository logementRepository;

  @Mock
  private EmailService emailService;

  @Test
  public void creerProprietaire_ReturnsProprietaireDto() {

    LocalDateTime localDateTime = LocalDateTime.now();

    ProprietaireCreationDto proprietaireCreationDto = ProprietaireCreationDto.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .adresse("13 FakeStreet City, 12345 Country")
        .lang("fr")
        .build();

    Proprietaire proprietaire = Proprietaire.builder()
        .nom(proprietaireCreationDto.getNom())
        .prenom(proprietaireCreationDto.getPrenom())
        .email(proprietaireCreationDto.getEmail())
        .numTel(proprietaireCreationDto.getNumTel())
        .motDePasse("password")
        .dateInscription(localDateTime)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    proprietaire.setLogements(null);
    proprietaire.setId(1L);

    when(proprietaireRepository.save(Mockito.any(Proprietaire.class))).thenReturn(proprietaire);

    ProprietaireDto savedProprietaire = proprietaireService.creerProprietaire(
        proprietaireCreationDto);

    Assertions.assertThat(savedProprietaire).isNotNull();
    Assertions.assertThat(savedProprietaire.getId()).isEqualTo(1L);
    Assertions.assertThat(savedProprietaire.getNom()).isEqualTo(proprietaireCreationDto.getNom());
    Assertions.assertThat(savedProprietaire.getPrenom())
        .isEqualTo(proprietaireCreationDto.getPrenom());
    Assertions.assertThat(savedProprietaire.getEmail())
        .isEqualTo(proprietaireCreationDto.getEmail());
    Assertions.assertThat(savedProprietaire.getNumTel())
        .isEqualTo(proprietaireCreationDto.getNumTel());
    Assertions.assertThat(savedProprietaire.getDateInscription()).isEqualTo(localDateTime);
  }

  @Test
  public void getProprietaire_ReturnsListProprietaireDto() {
    LocalDateTime localDateTime = LocalDateTime.now();

    Proprietaire proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("password")
        .dateInscription(localDateTime)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    proprietaire.setLogements(null);
    proprietaire.setId(1L);
    Proprietaire proprietaire2 = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("password")
        .dateInscription(localDateTime)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    proprietaire2.setLogements(null);
    proprietaire2.setId(2L);
    when(proprietaireRepository.findAll()).thenReturn(List.of(proprietaire, proprietaire2));

    List<ProprietaireDto> proprietaireDtos = proprietaireService.getProprietaire(false);

    Assertions.assertThat(proprietaireDtos).isNotNull();
    Assertions.assertThat(proprietaireDtos).hasSizeGreaterThan(1);

  }

  @Test
  public void getProprietaireById_ReturnOneProprietaireDto() {
    LocalDateTime localDateTime = LocalDateTime.now();

    Proprietaire proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("password")
        .dateInscription(localDateTime)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    proprietaire.setLogements(null);
    proprietaire.setId(1L);
    when(proprietaireRepository.findById(proprietaire.getId())).thenReturn(
        Optional.of(proprietaire));

    ProprietaireDto proprietaireDto = proprietaireService.getProprietaireById(proprietaire.getId(),
        false);

    Assertions.assertThat(proprietaireDto).isNotNull();
    Assertions.assertThat(proprietaireDto.getId()).isEqualTo(proprietaire.getId());
    Assertions.assertThat(proprietaireDto.getNom()).isEqualTo(proprietaire.getNom());
    Assertions.assertThat(proprietaireDto.getPrenom()).isEqualTo(proprietaire.getPrenom());
    Assertions.assertThat(proprietaireDto.getEmail()).isEqualTo(proprietaire.getEmail());
    Assertions.assertThat(proprietaireDto.getNumTel()).isEqualTo(proprietaire.getNumTel());
    Assertions.assertThat(proprietaireDto.getDateInscription())
        .isEqualTo(proprietaire.getDateInscription());

  }

  @Test
  public void getProprietaireByEmail_ReturnOneProprietaireDto() {
    LocalDateTime localDateTime = LocalDateTime.now();

    Proprietaire proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("password")
        .dateInscription(localDateTime)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    proprietaire.setLogements(null);
    proprietaire.setId(1L);
    when(proprietaireRepository.findFirstByEmail(proprietaire.getEmail())).thenReturn(
        Optional.of(proprietaire));

    ProprietaireDto proprietaireDto = proprietaireService.getProprietaireByEmail(
        proprietaire.getEmail());

    Assertions.assertThat(proprietaireDto).isNotNull();
    Assertions.assertThat(proprietaireDto.getId()).isEqualTo(proprietaire.getId());
    Assertions.assertThat(proprietaireDto.getNom()).isEqualTo(proprietaire.getNom());
    Assertions.assertThat(proprietaireDto.getPrenom()).isEqualTo(proprietaire.getPrenom());
    Assertions.assertThat(proprietaireDto.getEmail()).isEqualTo(proprietaire.getEmail());
    Assertions.assertThat(proprietaireDto.getNumTel()).isEqualTo(proprietaire.getNumTel());
    Assertions.assertThat(proprietaireDto.getDateInscription())
        .isEqualTo(proprietaire.getDateInscription());

  }

  @Test
  public void updateProprietaire_ReturnOneProprietaireDto() {
    LocalDateTime localDateTime = LocalDateTime.now();
    Long id = 1L;
    final ProprietaireCreationDto proprietaireCreationDto = ProprietaireCreationDto.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .lang("fr")
        .build();
    Proprietaire proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("password")
        .dateInscription(localDateTime)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    proprietaire.setLogements(null);
    proprietaire.setId(id);
    when(proprietaireRepository.save(Mockito.any(Proprietaire.class))).thenReturn(proprietaire);
    ProprietaireUpdateDto proprietaireUpdateDto = ProprietaireUpdateDto.builder()
        .nom("Doe")
        .build();
    when(proprietaireRepository.findById(id)).thenReturn(Optional.of(proprietaire));
    proprietaire.setNom(proprietaireUpdateDto.getNom());
    when(proprietaireRepository.save(any(Proprietaire.class))).thenReturn(proprietaire);

    ProprietaireDto proprietaireDtoUpdated = proprietaireService.updateProprietaire(id,
        proprietaireUpdateDto);

    Assertions.assertThat(proprietaireDtoUpdated).isNotNull();
    Assertions.assertThat(proprietaireDtoUpdated.getId()).isEqualTo(id);
    Assertions.assertThat(proprietaireDtoUpdated.getNom())
        .isEqualTo(proprietaireUpdateDto.getNom());
    Assertions.assertThat(proprietaireDtoUpdated.getPrenom())
        .isEqualTo(proprietaireCreationDto.getPrenom());
    Assertions.assertThat(proprietaireDtoUpdated.getEmail())
        .isEqualTo(proprietaireCreationDto.getEmail());
    Assertions.assertThat(proprietaireDtoUpdated.getNumTel())
        .isEqualTo(proprietaireCreationDto.getNumTel());
    Assertions.assertThat(proprietaireDtoUpdated.getDateInscription()).isEqualTo(localDateTime);
    Assertions.assertThat(proprietaireDtoUpdated.getLogements()).isNull();

  }

  @Test
  public void deleteProprietaire_ReturnOneProprietaireDto() {
    LocalDateTime localDateTime = LocalDateTime.now();
    Long id = 1L;

    Proprietaire proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("password")
        .dateInscription(localDateTime)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    proprietaire.setLogements(null);
    proprietaire.setId(id);
    when(proprietaireRepository.findById(id)).thenReturn(Optional.of(proprietaire));
    when(proprietaireRepository.save(any(Proprietaire.class))).thenReturn(proprietaire);
    doNothing().when(proprietaireRepository).deleteById(id);

    ProprietaireDto proprietaireDto = proprietaireService.deleteProprietaire(id);

    Assertions.assertThat(proprietaireDto).isNotNull();
    Assertions.assertThat(proprietaireDto.getId()).isEqualTo(id);
    Assertions.assertThat(proprietaireDto.getNom()).isEqualTo(proprietaire.getNom());
    Assertions.assertThat(proprietaireDto.getPrenom()).isEqualTo(proprietaire.getPrenom());
    Assertions.assertThat(proprietaireDto.getEmail()).isEqualTo(proprietaire.getEmail());
    Assertions.assertThat(proprietaireDto.getNumTel()).isEqualTo(proprietaire.getNumTel());
    Assertions.assertThat(proprietaireDto.getDateInscription()).isEqualTo(localDateTime);
    Assertions.assertThat(proprietaireDto.getLogements()).isNull();

  }

  @Test
  public void verifyPassword_ReturnTrue() {
    Long id = 1L;
    LocalDateTime localDateTime = LocalDateTime.now();
    String password = "password";
    String hashedPassword = "$2a$12$3hQDUblvPShmuQg/.g0Qk.wHAGjqPL54RMO/lNgsei/HQGo0ZLIYm";

    Proprietaire proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse(hashedPassword)
        .dateInscription(localDateTime)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    when(proprietaireRepository.findById(id)).thenReturn(Optional.ofNullable(proprietaire));

    boolean result = proprietaireService.verifyPassword(password, id);

    Assertions.assertThat(result).isTrue();


  }

  @Test
  public void verifyPassword_ReturnFalse() {
    Long id = 1L;
    LocalDateTime localDateTime = LocalDateTime.now();
    String password = "passwordFalse";
    String hashedPassword = "$2a$12$3hQDUblvPShmuQg/.g0Qk.wHAGjqPL54RMO/lNgsei/HQGo0ZLIYm";

    Proprietaire proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse(hashedPassword)
        .dateInscription(localDateTime)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    when(proprietaireRepository.findById(id)).thenReturn(Optional.ofNullable(proprietaire));

    boolean result = proprietaireService.verifyPassword(password, id);

    Assertions.assertThat(result).isFalse();


  }

}
