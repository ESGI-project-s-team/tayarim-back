package fr.esgi.al5.tayarim;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper; // Make sure to import ObjectMapper
import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.controllers.ProprietaireController;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireCreationDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireUpdateDto;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.services.AuthService;
import fr.esgi.al5.tayarim.services.ProprietaireService;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Classe de test du controller Proprietaire.
 */
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
  private ProprietaireCreationDto proprietaireCreationDto;
  private Proprietaire proprietaire;
  private List<ProprietaireDto> proprietaireDtos;
  private ProprietaireDto proprietaireDto;
  private ProprietaireDto proprietaireDto2;
  private ProprietaireDto proprietaireDtoWithLogement;
  private ProprietaireUpdateDto proprietaireUpdateDto;
  private String token = "";

  /**
   * methode exécuter avant chaque test.
   */
  @BeforeEach
  public void init() {
    token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huZG9lQ"
        + "GdtYWlsLmNvbTs2MjZjMjlhNS0zZGU0LTQ5Y2YtODI4ZS1hNDkxNWQzMzM5"
        + "N2EiLCJpYXQiOjE3MTQxMTM1NzcsImV4cCI6MTcxNDExNzE3N30.kbU1pVkUHkR"
        + "XktX44JBFN_xzDv-ZvSmtPjnjORO0vHPiHd3f2MGfDF15VTFO5icIrU_bV9cTqZ70"
        + "RDlKdlp0-g";
    LocalDateTime localDateTime = LocalDateTime.now();
    proprietaireCreationDto = ProprietaireCreationDto.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .build();
    proprietaire = Proprietaire.builder()
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .motDePasse("$2a$12$3hQDUblvPShmuQg/.g0Qk.wHAGjqPL54RMO/lNgsei/HQGo0ZLIYm")
        .dateInscription(localDateTime)
        .isPasswordUpdated(Boolean.TRUE)
        .build();
    proprietaireDto = ProprietaireDto.builder()
        .id(1L)
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .dateInscription(localDateTime)
        .logements(null)
        .isPasswordUpdated(Boolean.TRUE)
        .commission(20f)
        .build();
    proprietaireDto2 = ProprietaireDto.builder()
        .id(2L)
        .nom("Jhonny")
        .prenom("Pasdoe")
        .email("test2@gmail.com")
        .numTel("0612345679")
        .dateInscription(localDateTime)
        .logements(null)
        .isPasswordUpdated(Boolean.TRUE)
        .commission(20f)
        .build();
    proprietaireDtos = List.of(proprietaireDto, proprietaireDto2);
    proprietaireDtoWithLogement = ProprietaireDto.builder()
        .id(1L)
        .nom("Ferreira")
        .prenom("Mathieu")
        .email("test@gmail.com")
        .numTel("0612345678")
        .dateInscription(localDateTime)
        .isPasswordUpdated(Boolean.TRUE)
        .logements(List.of(new LogementDto(
            1L,
            "titre",
            1L,
            true,
            2,
            3,
            2,
            5,
            1,
            "description",
            0f,
            50f,
            "10:00",
            "12:00",
            "13 FakeStreet City, 12345 Country",
            "13 FakeStreet City",
            "City",
            "12345",
            "Country",
            null,
            null,
            "Appartement",
            "lienImage"
        )))
        .commission(20f)
        .build();
    proprietaireUpdateDto = ProprietaireUpdateDto.builder()
        .prenom("Karl")
        .build();
  }

  @Test
  public void creerProprietaire_ReturnCreated() throws Exception {
    given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(
        invocationOnMock -> proprietaireDto);
    when(authService.verifyToken(any(), anyBoolean())).thenReturn(
        new UserTokenInfo(proprietaireDto.getId(), true, true)); //skip token verif

    ResultActions response = mockMvc.perform(post("/proprietaires")
        .requestAttr("token", token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(proprietaireCreationDto)));

    response.andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.nom", CoreMatchers.is(proprietaireDto.getNom())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.prenom",
            CoreMatchers.is(proprietaireDto.getPrenom())))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(proprietaireDto.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.numTel",
            CoreMatchers.is(proprietaireDto.getNumTel())));
  }

  @Test
  public void creerProprietaire_ReturnErrorOwnerInvalidName()
      throws Exception {
    proprietaireCreationDto.setNom("");
    given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(
        invocationOnMock -> proprietaireDto);
    when(authService.verifyToken(any(), anyBoolean())).thenReturn(
        new UserTokenInfo(proprietaireDto.getId(), true, true)); //skip token verif

    ResultActions response = mockMvc.perform(post("/proprietaires")
        .requestAttr("token", token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(proprietaireCreationDto)));

    List<String> errors = List.of("error_owner_invalid_name");

    response.andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors", CoreMatchers.is(errors)));
  }

  @Test
  public void creerProprietaire_ReturnErrorOwnerInvalidFirstName()
      throws Exception {
    proprietaireCreationDto.setPrenom("");
    given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(
        invocationOnMock -> proprietaireDto);
    when(authService.verifyToken(any(), anyBoolean())).thenReturn(
        new UserTokenInfo(proprietaireDto.getId(), true, true)); //skip token verif

    ResultActions response = mockMvc.perform(post("/proprietaires")
        .requestAttr("token", token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(proprietaireCreationDto)));

    List<String> errors = List.of("error_owner_invalid_firstName");

    response.andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors", CoreMatchers.is(errors)));
  }

  @Test
  public void creerProprietaire_ReturnErrorOwnerInvalidMail_WhenEmailIsEmpty()
      throws Exception {
    proprietaireCreationDto.setEmail("");
    given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(
        invocationOnMock -> proprietaireDto);
    when(authService.verifyToken(any(), anyBoolean())).thenReturn(
        new UserTokenInfo(proprietaireDto.getId(), true, true)); //skip token verif

    ResultActions response = mockMvc.perform(post("/proprietaires")
        .requestAttr("token", token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(proprietaireCreationDto)));

    List<String> errors = List.of("error_owner_invalid_mail");

    response.andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors", CoreMatchers.is(errors)));
  }

  @Test
  public void creerProprietaire_ReturnErrorOwnerInvalidMail_WhenEmailDoesntMatchFormat()
      throws Exception {
    proprietaireCreationDto.setEmail("testmailcom");
    given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(
        invocationOnMock -> proprietaireDto);
    when(authService.verifyToken(any(), anyBoolean())).thenReturn(
        new UserTokenInfo(proprietaireDto.getId(), true, true)); //skip token verif

    ResultActions response = mockMvc.perform(post("/proprietaires")
        .requestAttr("token", token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(proprietaireCreationDto)));

    List<String> errors = List.of("error_owner_invalid_mail");

    response.andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors", CoreMatchers.is(errors)));
  }

  @Test
  public void creerProprietaire_ReturnErrorOwnerInvalidNumTel_WhenNumTelIsEmpty()
      throws Exception {
    proprietaireCreationDto.setNumTel("");
    given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(
        invocationOnMock -> proprietaireDto);
    when(authService.verifyToken(any(), anyBoolean())).thenReturn(
        new UserTokenInfo(proprietaireDto.getId(), true, true)); //skip token verif

    ResultActions response = mockMvc.perform(post("/proprietaires")
        .requestAttr("token", token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(proprietaireCreationDto)));

    List<String> errors = List.of("error_owner_invalid_phone");

    response.andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors", CoreMatchers.is(errors)));
  }

  @Test
  public void creerProprietaire_ReturnErrorOwnerInvalidNumTel_WhenNumTelDoesntMatchFormat()
      throws Exception {
    proprietaireCreationDto.setNumTel("aaaaa");
    given(proprietaireService.creerProprietaire(ArgumentMatchers.any())).willAnswer(
        invocationOnMock -> proprietaireDto);
    when(authService.verifyToken(any(), anyBoolean())).thenReturn(
        new UserTokenInfo(proprietaireDto.getId(), true, true)); //skip token verif

    ResultActions response = mockMvc.perform(post("/proprietaires")
        .requestAttr("token", token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(proprietaireCreationDto)));

    List<String> errors = List.of("error_owner_invalid_phone");

    response.andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors", CoreMatchers.is(errors)));
  }

  @Test
  public void getProprietaire_ReturnOk() throws Exception {
    given(proprietaireService.getProprietaire(false)).willAnswer(
        invocationOnMock -> proprietaireDtos);

    when(authService.verifyToken(any(), anyBoolean())).thenReturn(
        new UserTokenInfo(proprietaireDto.getId(), true, true)); //skip token verif

    ResultActions response = mockMvc.perform(get("/proprietaires")
        .requestAttr("token", token)
        .contentType(MediaType.APPLICATION_JSON));

    response.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[0].nom", CoreMatchers.is(proprietaireDto.getNom())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].prenom",
            CoreMatchers.is(proprietaireDto.getPrenom())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].email",
            CoreMatchers.is(proprietaireDto.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].numTel",
            CoreMatchers.is(proprietaireDto.getNumTel())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].logements",
            CoreMatchers.is(proprietaireDto.getLogements())))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$[1].nom", CoreMatchers.is(proprietaireDto2.getNom())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].prenom",
            CoreMatchers.is(proprietaireDto2.getPrenom())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].email",
            CoreMatchers.is(proprietaireDto2.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].numTel",
            CoreMatchers.is(proprietaireDto2.getNumTel())))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].logements",
            CoreMatchers.is(proprietaireDto2.getLogements())));
  }

  @Test
  public void getProprietaire_WithIdInPath_ReturnOk() throws Exception {
    given(proprietaireService.getProprietaireById(1L, false)).willAnswer(
        invocationOnMock -> proprietaireDto);

    when(authService.verifyToken(any(), anyBoolean())).thenReturn(
        new UserTokenInfo(proprietaireDto.getId(), true, true)); //skip token verif

    ResultActions response = mockMvc.perform(get("/proprietaires/1")
        .requestAttr("token", token)
        .contentType(MediaType.APPLICATION_JSON));

    response.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.nom", CoreMatchers.is(proprietaireDto.getNom())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.prenom",
            CoreMatchers.is(proprietaireDto.getPrenom())))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(proprietaireDto.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.numTel",
            CoreMatchers.is(proprietaireDto.getNumTel())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.logements",
            CoreMatchers.is(proprietaireDto.getLogements())));
  }

  @Test
  public void updateProprietaire_ReturnOk() throws Exception {
    proprietaireDto.setPrenom(proprietaireUpdateDto.getPrenom());
    given(proprietaireService.updateProprietaire(1L, proprietaireUpdateDto)).willAnswer(
        invocationOnMock -> proprietaireDto);

    when(authService.verifyToken(any(), anyBoolean())).thenReturn(
        new UserTokenInfo(proprietaireDto.getId(), true, true)); //skip token verif

    ResultActions response = mockMvc.perform(put("/proprietaires/1")
        .requestAttr("token", token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(proprietaireUpdateDto)));

    response.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.nom", CoreMatchers.is(proprietaireDto.getNom())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.prenom",
            CoreMatchers.is(proprietaireUpdateDto.getPrenom())))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(proprietaireDto.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.numTel",
            CoreMatchers.is(proprietaireDto.getNumTel())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.logements",
            CoreMatchers.is(proprietaireDto.getLogements())));
  }

  @Test
  public void deleteProprietaire_ReturnOk() throws Exception {
    given(proprietaireService.deleteProprietaire(1L)).willAnswer(
        invocationOnMock -> proprietaireDto);

    when(authService.verifyToken(any(), anyBoolean())).thenReturn(
        new UserTokenInfo(proprietaireDto.getId(), true, true)); //skip token verif

    ResultActions response = mockMvc.perform(delete("/proprietaires/1")
        .requestAttr("token", token)
        .contentType(MediaType.APPLICATION_JSON));

    response.andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.nom", CoreMatchers.is(proprietaireDto.getNom())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.prenom",
            CoreMatchers.is(proprietaireDto.getPrenom())))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(proprietaireDto.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.numTel",
            CoreMatchers.is(proprietaireDto.getNumTel())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.logements",
            CoreMatchers.is(proprietaireDto.getLogements())));
  }

}

