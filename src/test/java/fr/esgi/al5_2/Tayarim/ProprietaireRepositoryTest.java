package fr.esgi.al5_2.Tayarim;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import fr.esgi.al5_2.Tayarim.repositories.ProprietaireRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProprietaireRepositoryTest {

    @Autowired
    private ProprietaireRepository proprietaireRepository;

    @Test
    public void ProprietaireRepository_Save_ReturnSavedProprietaire() {

        LocalDateTime dateInscription = LocalDateTime.now();
        Proprietaire proprietaire = Proprietaire.builder()
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .motDePasse("password")
                .dateInscription(dateInscription)
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
    public void ProprietaireRepository_FindAll_ReturnZeroProprietaire() {

        List<Proprietaire> proprietaires = proprietaireRepository.findAll();

        assertThat(proprietaires).isNotNull();
        assertThat(proprietaires).hasSize(0);
    }
    @Test
    public void ProprietaireRepository_FindAll_ReturnOneProprietaire() {

        LocalDateTime dateInscription = LocalDateTime.now();
        Proprietaire proprietaire = Proprietaire.builder()
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .motDePasse("password")
                .dateInscription(dateInscription)
                .build();
        proprietaireRepository.save(proprietaire);

        List<Proprietaire> proprietaires = proprietaireRepository.findAll();

        assertThat(proprietaires).isNotNull();
        assertThat(proprietaires).hasSize(1);
    }

    @Test
    public void ProprietaireRepository_FindAll_ReturnManyProprietaire() {

        LocalDateTime dateInscription = LocalDateTime.now();
        Proprietaire proprietaire = Proprietaire.builder()
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .motDePasse("password")
                .dateInscription(dateInscription)
                .build();
        Proprietaire proprietaire2 = Proprietaire.builder()
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .motDePasse("password")
                .dateInscription(dateInscription)
                .build();
        proprietaireRepository.save(proprietaire);
        proprietaireRepository.save(proprietaire2);

        List<Proprietaire> proprietaires = proprietaireRepository.findAll();

        assertThat(proprietaires).isNotNull();
        assertThat(proprietaires).hasSize(2);
    }

    @Test
    public void ProprietaireRepository_FindFirstByEmail_ReturnOneProprietaire() {

        LocalDateTime dateInscription = LocalDateTime.now();
        Proprietaire proprietaire = Proprietaire.builder()
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .motDePasse("password")
                .dateInscription(dateInscription)
                .build();
        proprietaireRepository.save(proprietaire);

        Proprietaire findProprietaire = proprietaireRepository.findFirstByEmail(proprietaire.getEmail()).get();

        assertThat(findProprietaire).isNotNull();
        assertThat(findProprietaire.getId()).isNotNull();
        assertThat(findProprietaire.getNom()).isEqualTo(proprietaire.getNom());
        assertThat(findProprietaire.getPrenom()).isEqualTo(proprietaire.getPrenom());
        assertThat(findProprietaire.getEmail()).isEqualTo(proprietaire.getEmail());
        assertThat(findProprietaire.getNumTel()).isEqualTo(proprietaire.getNumTel());
        assertThat(findProprietaire.getMotDePasse()).isEqualTo(proprietaire.getMotDePasse());
        assertThat(findProprietaire.getDateInscription()).isEqualTo(proprietaire.getDateInscription());
    }


}
