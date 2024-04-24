package fr.esgi.al5_2.Tayarim;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import fr.esgi.al5_2.Tayarim.repositories.ProprietaireRepository;
import fr.esgi.al5_2.Tayarim.services.ProprietaireService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ProprietaireServiceTest {

    @Mock
    private ProprietaireRepository proprietaireRepository;

    @InjectMocks
    private ProprietaireService proprietaireService;

    @Test
    public void ProprietaireService_CreerProprietaire_ReturnsProprietaireDto() {

        LocalDateTime localDateTime = LocalDateTime.now();

        ProprietaireCreationDTO proprietaireCreationDTO = ProprietaireCreationDTO.builder()
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .motDePasse("password")
                .build();

        Proprietaire proprietaire = Proprietaire.builder()
                .nom(proprietaireCreationDTO.getNom())
                .prenom(proprietaireCreationDTO.getPrenom())
                .email(proprietaireCreationDTO.getEmail())
                .numTel(proprietaireCreationDTO.getNumTel())
                .motDePasse(proprietaireCreationDTO.getMotDePasse())
                .dateInscription(localDateTime)
                .build();
        proprietaire.setLogements(null);
        proprietaire.setId(1L);

        when(proprietaireRepository.save(Mockito.any(Proprietaire.class))).thenReturn(proprietaire);


        ProprietaireDTO savedProprietaire = proprietaireService.creerProprietaire(proprietaireCreationDTO);


        Assertions.assertThat(savedProprietaire).isNotNull();
        Assertions.assertThat(savedProprietaire.getId()).isEqualTo(1L);
        Assertions.assertThat(savedProprietaire.getNom()).isEqualTo(proprietaireCreationDTO.getNom());
        Assertions.assertThat(savedProprietaire.getPrenom()).isEqualTo(proprietaireCreationDTO.getPrenom());
        Assertions.assertThat(savedProprietaire.getEmail()).isEqualTo(proprietaireCreationDTO.getEmail());
        Assertions.assertThat(savedProprietaire.getNumTel()).isEqualTo(proprietaireCreationDTO.getNumTel());
        Assertions.assertThat(savedProprietaire.getDateInscription()).isEqualTo(localDateTime);
    }

    @Test
    public void ProprietaireService_GetProprietaire_ReturnsListProprietaireDto(){
        LocalDateTime localDateTime = LocalDateTime.now();

        Proprietaire proprietaire = Proprietaire.builder()
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .motDePasse("password")
                .dateInscription(localDateTime)
                .build();
        proprietaire.setLogements(null);
        proprietaire.setId(1L);

        when(proprietaireRepository.findAll()).thenReturn(List.of(proprietaire));


        List<ProprietaireDTO> proprietaireDTOS = proprietaireService.getProprietaire(false);

        Assertions.assertThat(proprietaireDTOS).isNotNull();
        Assertions.assertThat(proprietaireDTOS).hasSize(1);

    }

}
