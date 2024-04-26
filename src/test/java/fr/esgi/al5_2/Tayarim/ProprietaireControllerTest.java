package fr.esgi.al5_2.Tayarim;

import fr.esgi.al5_2.Tayarim.auth.AuthTokenFilter;
import fr.esgi.al5_2.Tayarim.dto.logement.LogementDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import fr.esgi.al5_2.Tayarim.repositories.ProprietaireRepository;
import fr.esgi.al5_2.Tayarim.services.AuthService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import fr.esgi.al5_2.Tayarim.services.ProprietaireService;
import fr.esgi.al5_2.Tayarim.controllers.ProprietaireController;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper; // Make sure to import ObjectMapper
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(controllers = ProprietaireController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProprietaireControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProprietaireService proprietaireService;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;
    private ProprietaireCreationDTO proprietaireCreationDTO;
    private Proprietaire proprietaire;
    private ProprietaireDTO proprietaireDTO;
    private ProprietaireDTO proprietaireDTOWithLogement;

    @BeforeEach
    public void init() {
        LocalDateTime localDateTime = LocalDateTime.now();
        proprietaireCreationDTO = ProprietaireCreationDTO.builder()
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .motDePasse("password")
                .build();
        proprietaire = Proprietaire.builder()
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .motDePasse("$2a$12$3hQDUblvPShmuQg/.g0Qk.wHAGjqPL54RMO/lNgsei/HQGo0ZLIYm")
                .dateInscription(LocalDateTime.now())
                .build();
        proprietaireDTO = ProprietaireDTO.builder()
                .id(1L)
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .dateInscription(localDateTime)
                .logements(null)
                .build();
        proprietaireDTOWithLogement = ProprietaireDTO.builder()
                .id(1L)
                .nom("Ferreira")
                .prenom("Mathieu")
                .email("test@gmail.com")
                .numTel("0612345678")
                .dateInscription(localDateTime)
                .logements(List.of(new LogementDTO(1L, 1L)))
                .build();
    }

    @Test
    public void ProprietaireController_CreerProprietaire_ReturnCreated() throws Exception {
        given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(invocationOnMock -> proprietaireDTO);

        ResultActions response = mockMvc.perform(post("/proprietaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proprietaireCreationDTO)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nom", CoreMatchers.is(proprietaireDTO.getNom())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.prenom", CoreMatchers.is(proprietaireDTO.getPrenom())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(proprietaireDTO.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numTel", CoreMatchers.is(proprietaireDTO.getNumTel())));
    }

    @Test
    public void ProprietaireController_CreerProprietaire_ReturnErrorOwnerInvalidName() throws Exception {
        proprietaireCreationDTO.setNom("");
        given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(invocationOnMock -> proprietaireDTO);

        ResultActions response = mockMvc.perform(post("/proprietaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proprietaireCreationDTO)));

        List<String> errors = List.of("error_owner_invalid_name");

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", CoreMatchers.is(errors)));
    }

    @Test
    public void ProprietaireController_CreerProprietaire_ReturnErrorOwnerInvalidFirstName() throws Exception {
        proprietaireCreationDTO.setPrenom("");
        given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(invocationOnMock -> proprietaireDTO);

        ResultActions response = mockMvc.perform(post("/proprietaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proprietaireCreationDTO)));

        List<String> errors = List.of("error_owner_invalid_firstName");

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", CoreMatchers.is(errors)));
    }

    @Test
    public void ProprietaireController_CreerProprietaire_ReturnErrorOwnerInvalidMail_WhenEmailIsEmpty() throws Exception {
        proprietaireCreationDTO.setEmail("");
        given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(invocationOnMock -> proprietaireDTO);

        ResultActions response = mockMvc.perform(post("/proprietaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proprietaireCreationDTO)));

        List<String> errors = List.of("error_owner_invalid_mail");

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", CoreMatchers.is(errors)));
    }

    @Test
    public void ProprietaireController_CreerProprietaire_ReturnErrorOwnerInvalidMail_WhenEmailDoesntMatchFormat() throws Exception {
        proprietaireCreationDTO.setEmail("testmailcom");
        given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(invocationOnMock -> proprietaireDTO);

        ResultActions response = mockMvc.perform(post("/proprietaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proprietaireCreationDTO)));

        List<String> errors = List.of("error_owner_invalid_mail");

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", CoreMatchers.is(errors)));
    }

    @Test
    public void ProprietaireController_CreerProprietaire_ReturnErrorOwnerInvalidNumTel_WhenNumTelIsEmpty() throws Exception {
        proprietaireCreationDTO.setNumTel("");
        given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(invocationOnMock -> proprietaireDTO);

        ResultActions response = mockMvc.perform(post("/proprietaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proprietaireCreationDTO)));

        List<String> errors = List.of("error_owner_invalid_phone", "error_owner_invalid_phone"); // 2 error because it also doesnt match Regex

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", CoreMatchers.is(errors)));
    }

    @Test
    public void ProprietaireController_CreerProprietaire_ReturnErrorOwnerInvalidNumTel_WhenNumTelDoesntMatchFormat() throws Exception {
        proprietaireCreationDTO.setNumTel("aaaaa");
        given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(invocationOnMock -> proprietaireDTO);

        ResultActions response = mockMvc.perform(post("/proprietaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proprietaireCreationDTO)));

        List<String> errors = List.of("error_owner_invalid_phone");

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", CoreMatchers.is(errors)));
    }

    @Test
    public void ProprietaireController_CreerProprietaire_ReturnErrorOwnerInvalidMotDePasse() throws Exception {
        proprietaireCreationDTO.setMotDePasse("");
        given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(invocationOnMock -> proprietaireDTO);

        ResultActions response = mockMvc.perform(post("/proprietaires")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proprietaireCreationDTO)));

        List<String> errors = List.of("error_owner_invalid_password");

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", CoreMatchers.is(errors)));
    }

}

