package fr.esgi.al5_2.Tayarim;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import fr.esgi.al5_2.Tayarim.Services.ProprietaireService;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import fr.esgi.al5_2.Tayarim.mappers.ProprietaireMapper;
import fr.esgi.al5_2.Tayarim.repositories.ProprietaireRepository;

@ExtendWith(MockitoExtension.class)
public class ProprietaireServiceTest {

    @InjectMocks
    private ProprietaireService proprietaireService;

    @Mock
    private ProprietaireRepository proprietaireRepository; // Mock the repository

    @Test
    @Transactional
    public void testCreateUser() {
        ProprietaireCreationDTO proprietaireCreationDTO = new ProprietaireCreationDTO(
            "Ferreira", 
            "Mathieu", 
            "test@gmail.com", 
            "0612345678", 
            "password");
        // Set other required fields...

        LocalDateTime dateInscription = LocalDateTime.now();
        Long id = 1L;
        Proprietaire mockProprietaire = new Proprietaire(
            proprietaireCreationDTO.getNom(), 
            proprietaireCreationDTO.getPrenom(), 
            proprietaireCreationDTO.getEmail(), 
            proprietaireCreationDTO.getNumTel(), 
            proprietaireCreationDTO.getMotDePasse(), 
            dateInscription);
            mockProprietaire.setId(id);

        when(proprietaireRepository.save(any(Proprietaire.class))).thenReturn(mockProprietaire);
        ProprietaireDTO proprietaireDTO = proprietaireService.creeProprietaire(proprietaireCreationDTO, false);

        assertThat(proprietaireDTO).isNotNull();
        assertThat(proprietaireDTO.getId()).isEqualTo(id);
        assertThat(proprietaireDTO.getNom()).isEqualTo(proprietaireCreationDTO.getNom());
        assertThat(proprietaireDTO.getPrenom()).isEqualTo(proprietaireCreationDTO.getPrenom());
        assertThat(proprietaireDTO.getEmail()).isEqualTo(proprietaireCreationDTO.getEmail());
        assertThat(proprietaireDTO.getNumTel()).isEqualTo(proprietaireCreationDTO.getNumTel());
        assertThat(proprietaireDTO.getMotDePasse()).isNull();
        assertThat(proprietaireDTO.getDateInscription()).isEqualTo(dateInscription);
        assertThat(proprietaireDTO.getLogements()).isNull();
        // Assert other fields as necessary...

    }
}
