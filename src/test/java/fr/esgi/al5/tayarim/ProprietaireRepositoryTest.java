package fr.esgi.al5.tayarim;

import static org.assertj.core.api.Assertions.assertThat;

import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.repositories.ProprietaireRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Classe de test du repository Proprietaire.
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProprietaireRepositoryTest {

  @Autowired
  private ProprietaireRepository proprietaireRepository;

  @Test
  public void save_ReturnSavedProprietaire() {

    LocalDateTime dateInscription = LocalDateTime.now();
    Proprietaire proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("password")
        .dateInscription(dateInscription)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();

    Proprietaire savedProprietaire = proprietaireRepository.save(proprietaire);

    assertThat(savedProprietaire).isNotNull();
    assertThat(savedProprietaire.getId()).isNotNull();
    assertThat(savedProprietaire.getId()).isEqualTo(1L);
    assertThat(savedProprietaire.getNom()).isEqualTo(proprietaire.getNom());
    assertThat(savedProprietaire.getPrenom()).isEqualTo(proprietaire.getPrenom());
    assertThat(savedProprietaire.getEmail()).isEqualTo(proprietaire.getEmail());
    assertThat(savedProprietaire.getNumTel()).isEqualTo(proprietaire.getNumTel());
    assertThat(savedProprietaire.getMotDePasse()).isEqualTo(proprietaire.getMotDePasse());
    assertThat(savedProprietaire.getDateInscription()).isEqualTo(proprietaire.getDateInscription());
  }

  @Test
  public void findAll_ReturnZeroProprietaire() {

    List<Proprietaire> proprietaires = proprietaireRepository.findAll();

    assertThat(proprietaires).isNotNull();
    assertThat(proprietaires).hasSize(0);
  }

  @Test
  public void findAll_ReturnOneProprietaire() {

    LocalDateTime dateInscription = LocalDateTime.now();
    Proprietaire proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("password")
        .dateInscription(dateInscription)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    proprietaireRepository.save(proprietaire);

    List<Proprietaire> proprietaires = proprietaireRepository.findAll();

    assertThat(proprietaires).isNotNull();
    assertThat(proprietaires).hasSize(1);
  }

  @Test
  public void findAll_ReturnManyProprietaire() {

    LocalDateTime dateInscription = LocalDateTime.now();
    Proprietaire proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("password")
        .dateInscription(dateInscription)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    Proprietaire proprietaire2 = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("password")
        .dateInscription(dateInscription)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    proprietaireRepository.save(proprietaire);
    proprietaireRepository.save(proprietaire2);

    List<Proprietaire> proprietaires = proprietaireRepository.findAll();

    assertThat(proprietaires).isNotNull();
    assertThat(proprietaires).hasSizeGreaterThan(1);
  }

  @Test
  public void findFirstByEmail_ReturnProprietaire() {

    LocalDateTime dateInscription = LocalDateTime.now();
    Proprietaire proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("password")
        .dateInscription(dateInscription)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    proprietaireRepository.save(proprietaire);

    Proprietaire findProprietaire = proprietaireRepository.findFirstByEmail(proprietaire.getEmail())
        .get();

    assertThat(findProprietaire).isNotNull();
    assertThat(findProprietaire.getId()).isNotNull();
    assertThat(findProprietaire.getNom()).isEqualTo(proprietaire.getNom());
    assertThat(findProprietaire.getPrenom()).isEqualTo(proprietaire.getPrenom());
    assertThat(findProprietaire.getEmail()).isEqualTo(proprietaire.getEmail());
    assertThat(findProprietaire.getNumTel()).isEqualTo(proprietaire.getNumTel());
    assertThat(findProprietaire.getMotDePasse()).isEqualTo(proprietaire.getMotDePasse());
    assertThat(findProprietaire.getDateInscription()).isEqualTo(proprietaire.getDateInscription());
  }

  @Test
  public void findFirstByNumTel_ReturnProprietaire() {

    LocalDateTime dateInscription = LocalDateTime.now();
    Proprietaire proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("password")
        .dateInscription(dateInscription)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    proprietaireRepository.save(proprietaire);

    Proprietaire findProprietaire = proprietaireRepository.findFirstByNumTel(
        proprietaire.getNumTel()).get();

    assertThat(findProprietaire).isNotNull();
    assertThat(findProprietaire.getId()).isNotNull();
    assertThat(findProprietaire.getNom()).isEqualTo(proprietaire.getNom());
    assertThat(findProprietaire.getPrenom()).isEqualTo(proprietaire.getPrenom());
    assertThat(findProprietaire.getEmail()).isEqualTo(proprietaire.getEmail());
    assertThat(findProprietaire.getNumTel()).isEqualTo(proprietaire.getNumTel());
    assertThat(findProprietaire.getMotDePasse()).isEqualTo(proprietaire.getMotDePasse());
    assertThat(findProprietaire.getDateInscription()).isEqualTo(proprietaire.getDateInscription());
  }

  @Test
  public void deleteById_ReturnProprietaire() {

    LocalDateTime dateInscription = LocalDateTime.now();
    Proprietaire proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("password")
        .dateInscription(dateInscription)
        .isPasswordUpdated(Boolean.TRUE)
        .adresse("13 FakeStreet City, 12345 Country")
        .isValidated(Boolean.TRUE)
        .langue("fr")
        .build();
    proprietaireRepository.save(proprietaire);

    proprietaireRepository.deleteById(proprietaire.getId());

    List<Proprietaire> proprietaires = proprietaireRepository.findAll();
    assertThat(proprietaires).isNotNull();
    assertThat(proprietaires).hasSize(0);
  }

}
