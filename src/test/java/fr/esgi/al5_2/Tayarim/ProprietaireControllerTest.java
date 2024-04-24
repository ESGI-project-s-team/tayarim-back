package fr.esgi.al5_2.Tayarim;

import fr.esgi.al5_2.Tayarim.auth.AuthTokenFilter;
import fr.esgi.al5_2.Tayarim.repositories.ProprietaireRepository;
import fr.esgi.al5_2.Tayarim.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper; // Make sure to import ObjectMapper

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProprietaireController.class)
public class ProprietaireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private AuthTokenFilter authTokenFilter;

    @Autowired
    private ObjectMapper objectMapper; // Spring Boot will automatically provide an ObjectMapper bean

    @Test
    public void shouldCreateUser() throws Exception {

        ProprietaireCreationDTO proprietaireCreationDTO = new ProprietaireCreationDTO(
        "Ferreira", 
        "Mathieu", 
        "test@gmail.com", 
        "0612345678", 
        "password");

        mockMvc.perform(post("/proprietaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(proprietaireCreationDTO))) // Convert the object to JSON string // Set your validator here
            .andExpect(status().isCreated());

    }

}

