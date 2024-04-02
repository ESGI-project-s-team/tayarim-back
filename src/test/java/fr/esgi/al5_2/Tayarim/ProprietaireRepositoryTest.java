package fr.esgi.al5_2.Tayarim;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import fr.esgi.al5_2.Tayarim.repositories.ProprietaireRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
public class ProprietaireRepositoryTest {

    @Autowired
    private ProprietaireRepository proprietaireRepository;

    @Test
    public void testCreateUser() {

        LocalDateTime dateInscription = LocalDateTime.now();

        Proprietaire newProprietaire = new Proprietaire(
            "Ferreira", 
            "Mathieu", 
            "test@gmail.com", 
            "0612345678", 
            "password", 
            dateInscription);

        Proprietaire savedProprietaire = proprietaireRepository.save(newProprietaire);
        assertThat(savedProprietaire).isNotNull();
        assertThat(savedProprietaire.getId()).isNotNull();
        assertThat(savedProprietaire.getNom()).isEqualTo("Ferreira");
        assertThat(savedProprietaire.getPrenom()).isEqualTo("Mathieu");
        assertThat(savedProprietaire.getEmail()).isEqualTo("test@gmail.com");
        assertThat(savedProprietaire.getNumTel()).isEqualTo("0612345678");
        assertThat(savedProprietaire.getMotDePasse()).isEqualTo("password");
        assertThat(savedProprietaire.getDateInscription()).isEqualTo(dateInscription);
        // Assert other fields as necessary...

        List<Proprietaire> proprietaires = proprietaireRepository.findAll();
        assertThat(proprietaires).isNotNull();
        assertThat(proprietaires).isNotEmpty();
        assertThat(proprietaires.size()).isEqualTo(1);
        assertThat(proprietaires.getFirst()).isEqualTo(savedProprietaire);
    }
}
