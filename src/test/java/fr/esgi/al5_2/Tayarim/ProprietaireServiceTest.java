package fr.esgi.al5_2.Tayarim;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireUpdateDTO;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
        Proprietaire proprietaire2 = Proprietaire.builder()
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .motDePasse("password")
                .dateInscription(localDateTime)
                .build();
        proprietaire2.setLogements(null);
        proprietaire2.setId(2L);
        when(proprietaireRepository.findAll()).thenReturn(List.of(proprietaire, proprietaire2));


        List<ProprietaireDTO> proprietaireDTOS = proprietaireService.getProprietaire(false);

        Assertions.assertThat(proprietaireDTOS).isNotNull();
        Assertions.assertThat(proprietaireDTOS).hasSizeGreaterThan(1);

    }

    @Test
    public void ProprietaireService_GetProprietaireById_ReturnOneProprietaireDto(){
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
        when(proprietaireRepository.findById(proprietaire.getId())).thenReturn(Optional.of(proprietaire));

        ProprietaireDTO proprietaireDTO = proprietaireService.getProprietaireById(proprietaire.getId(),false);

        Assertions.assertThat(proprietaireDTO).isNotNull();
        Assertions.assertThat(proprietaireDTO.getId()).isEqualTo(proprietaire.getId());
        Assertions.assertThat(proprietaireDTO.getNom()).isEqualTo(proprietaire.getNom());
        Assertions.assertThat(proprietaireDTO.getPrenom()).isEqualTo(proprietaire.getPrenom());
        Assertions.assertThat(proprietaireDTO.getEmail()).isEqualTo(proprietaire.getEmail());
        Assertions.assertThat(proprietaireDTO.getNumTel()).isEqualTo(proprietaire.getNumTel());
        Assertions.assertThat(proprietaireDTO.getDateInscription()).isEqualTo(proprietaire.getDateInscription());

    }

    @Test
    public void ProprietaireService_GetProprietaireByEmail_ReturnOneProprietaireDto(){
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
        when(proprietaireRepository.findFirstByEmail(proprietaire.getEmail())).thenReturn(Optional.of(proprietaire));

        ProprietaireDTO proprietaireDTO = proprietaireService.getProprietaireByEmail(proprietaire.getEmail());

        Assertions.assertThat(proprietaireDTO).isNotNull();
        Assertions.assertThat(proprietaireDTO.getId()).isEqualTo(proprietaire.getId());
        Assertions.assertThat(proprietaireDTO.getNom()).isEqualTo(proprietaire.getNom());
        Assertions.assertThat(proprietaireDTO.getPrenom()).isEqualTo(proprietaire.getPrenom());
        Assertions.assertThat(proprietaireDTO.getEmail()).isEqualTo(proprietaire.getEmail());
        Assertions.assertThat(proprietaireDTO.getNumTel()).isEqualTo(proprietaire.getNumTel());
        Assertions.assertThat(proprietaireDTO.getDateInscription()).isEqualTo(proprietaire.getDateInscription());

    }

    @Test
    public void ProprietaireService_UpdateProprietaire_ReturnOneProprietaireDto(){
        LocalDateTime localDateTime = LocalDateTime.now();
        Long id = 1L;
        ProprietaireCreationDTO proprietaireCreationDTO = ProprietaireCreationDTO.builder()
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .motDePasse("password")
                .build();
        Proprietaire proprietaire = Proprietaire.builder()
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .motDePasse("password")
                .dateInscription(localDateTime)
                .build();
        proprietaire.setLogements(null);
        proprietaire.setId(id);
        when(proprietaireRepository.save(Mockito.any(Proprietaire.class))).thenReturn(proprietaire);
        ProprietaireDTO proprietaireDTO = proprietaireService.creerProprietaire(proprietaireCreationDTO);
        ProprietaireUpdateDTO proprietaireUpdateDTO = ProprietaireUpdateDTO.builder()
                .nom("Doe")
                .build();
        when(proprietaireRepository.findById(id)).thenReturn(Optional.of(proprietaire));
        proprietaire.setNom(proprietaireUpdateDTO.getNom());
        when(proprietaireRepository.save(any(Proprietaire.class))).thenReturn(proprietaire);

        ProprietaireDTO proprietaireDTOUpdated = proprietaireService.updateProprietaire(id, proprietaireUpdateDTO);

        Assertions.assertThat(proprietaireDTOUpdated).isNotNull();
        Assertions.assertThat(proprietaireDTOUpdated.getId()).isEqualTo(id);
        Assertions.assertThat(proprietaireDTOUpdated.getNom()).isEqualTo(proprietaireUpdateDTO.getNom());
        Assertions.assertThat(proprietaireDTOUpdated.getPrenom()).isEqualTo(proprietaireCreationDTO.getPrenom());
        Assertions.assertThat(proprietaireDTOUpdated.getEmail()).isEqualTo(proprietaireCreationDTO.getEmail());
        Assertions.assertThat(proprietaireDTOUpdated.getNumTel()).isEqualTo(proprietaireCreationDTO.getNumTel());
        Assertions.assertThat(proprietaireDTOUpdated.getDateInscription()).isEqualTo(localDateTime);
        Assertions.assertThat(proprietaireDTOUpdated.getLogements()).isNull();

    }

    @Test
    public void ProprietaireService_DeleteProprietaire_ReturnOneProprietaireDto(){
        LocalDateTime localDateTime = LocalDateTime.now();
        Long id = 1L;

        Proprietaire proprietaire = Proprietaire.builder()
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .motDePasse("password")
                .dateInscription(localDateTime)
                .build();
        proprietaire.setLogements(null);
        proprietaire.setId(id);
        when(proprietaireRepository.findById(id)).thenReturn(Optional.of(proprietaire));
        when(proprietaireRepository.save(any(Proprietaire.class))).thenReturn(proprietaire);
        doNothing().when(proprietaireRepository).deleteById(id);

        ProprietaireDTO proprietaireDTO = proprietaireService.deleteProprietaire(id);

        Assertions.assertThat(proprietaireDTO).isNotNull();
        Assertions.assertThat(proprietaireDTO.getId()).isEqualTo(id);
        Assertions.assertThat(proprietaireDTO.getNom()).isEqualTo(proprietaire.getNom());
        Assertions.assertThat(proprietaireDTO.getPrenom()).isEqualTo(proprietaire.getPrenom());
        Assertions.assertThat(proprietaireDTO.getEmail()).isEqualTo(proprietaire.getEmail());
        Assertions.assertThat(proprietaireDTO.getNumTel()).isEqualTo(proprietaire.getNumTel());
        Assertions.assertThat(proprietaireDTO.getDateInscription()).isEqualTo(localDateTime);
        Assertions.assertThat(proprietaireDTO.getLogements()).isNull();

    }

    @Test
    public void ProprietaireService_VerifyPassword_ReturnTrue(){
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
                .build();
        when(proprietaireRepository.findById(id)).thenReturn(Optional.ofNullable(proprietaire));

        boolean result = proprietaireService.verifyPassword(password, id);

        Assertions.assertThat(result).isTrue();


    }

    @Test
    public void ProprietaireService_VerifyPassword_ReturnFalse(){
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
                .build();
        when(proprietaireRepository.findById(id)).thenReturn(Optional.ofNullable(proprietaire));

        boolean result = proprietaireService.verifyPassword(password, id);

        Assertions.assertThat(result).isFalse();


    }

}
