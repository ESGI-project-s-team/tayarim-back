package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.controllers.interfaces.DeleteMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.UpdateMethodInterface;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireCandidateDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireCreationDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireUpdateDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireValidateDto;
import fr.esgi.al5.tayarim.exceptions.PasswordHashNotPossibleException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireEmailAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNumTelAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.TokenExpireOrInvalidException;
import fr.esgi.al5.tayarim.exceptions.UnauthorizedException;
import fr.esgi.al5.tayarim.services.AuthService;
import fr.esgi.al5.tayarim.services.ProprietaireService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des propriétaires.
 */
@RestController
@RequestMapping("/proprietaires")
public class ProprietaireController implements
    UpdateMethodInterface<ProprietaireDto, ProprietaireUpdateDto>,
    DeleteMethodInterface<ProprietaireDto>,
    ControllerUtils {

  private final ProprietaireService proprietaireService;
  private final AuthService authService;

  /**
   * Constructeur pour initialiser les services de gestion des propriétaires et d'authentification.
   *
   * @param proprietaireService Le service de gestion des propriétaires.
   * @param authService         Le service d'authentification.
   */
  public ProprietaireController(ProprietaireService proprietaireService, AuthService authService) {
    this.proprietaireService = proprietaireService;
    this.authService = authService;
  }

  /**
   * Crée un nouveau propriétaire.
   *
   * @param authHeader              L'en-tête d'autorisation contenant le token JWT.
   * @param proprietaireCreationDto Les données pour la création d'un propriétaire.
   * @return Une ResponseEntity contenant le propriétaire créé et le statut HTTP.
   */
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @PostMapping("")
  public ResponseEntity<ProprietaireDto> create(
      @RequestAttribute("token") String authHeader,
      @Valid @RequestBody ProprietaireCreationDto proprietaireCreationDto) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);
    return new ResponseEntity<>(
        proprietaireService.creerProprietaire(proprietaireCreationDto),
        HttpStatus.CREATED);
  }

  /**
   * Obtient la liste de tous les propriétaires.
   *
   * @param authHeader L'en-tête d'autorisation contenant le token JWT.
   * @param isLogement Indicateur si les propriétaires retournés doivent inclure des informations de
   *                   logement.
   * @return Une ResponseEntity contenant la liste des propriétaires et le statut HTTP.
   */
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @GetMapping("")
  public ResponseEntity<List<ProprietaireDto>> getAll(
      @RequestAttribute("token") String authHeader,
      @RequestParam(name = "logement", defaultValue = "false") Boolean isLogement) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);

    return new ResponseEntity<>(
        proprietaireService.getProprietaire(isLogement),
        HttpStatus.OK
    );
  }

  /**
   * Obtient un propriétaire par son identifiant.
   *
   * @param authHeader L'en-tête d'autorisation contenant le token JWT.
   * @param id         L'identifiant du propriétaire.
   * @param isLogement Indicateur si le propriétaire retourné doit inclure des informations de
   *                   logement.
   * @return Une ResponseEntity contenant les détails du propriétaire et le statut HTTP.
   */
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @GetMapping("/{id}")
  public ResponseEntity<ProprietaireDto> getById(
      @RequestAttribute("token") String authHeader, @PathVariable Long id,
      @RequestParam(name = "logement", defaultValue = "false") Boolean isLogement) {

    authService.verifyToken(getTokenFromHeader(authHeader), false);

    return new ResponseEntity<>(
        proprietaireService.getProprietaireById(id, isLogement),
        HttpStatus.OK
    );
  }

  /**
   * Met à jour les informations d'un propriétaire.
   *
   * @param authHeader            L'en-tête d'autorisation contenant le token JWT.
   * @param id                    L'identifiant du propriétaire à mettre à jour.
   * @param proprietaireUpdateDto Les données de mise à jour du propriétaire.
   * @return Une ResponseEntity contenant le propriétaire mis à jour et le statut HTTP.
   */
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @PutMapping("/{id}")
  public ResponseEntity<ProprietaireDto> update(
      @RequestAttribute("token") String authHeader, @PathVariable Long id,
      @Valid @RequestBody ProprietaireUpdateDto proprietaireUpdateDto) {
    UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), false);
    if (!userTokenInfo.getId().equals(id) && !userTokenInfo.getIsAdmin()) {
      throw new UnauthorizedException();
    }

    return new ResponseEntity<>(
        proprietaireService.updateProprietaire(id, proprietaireUpdateDto),
        HttpStatus.OK
    );
  }

  /**
   * Supprime un propriétaire par son identifiant.
   *
   * @param authHeader L'en-tête d'autorisation contenant le token JWT.
   * @param id         L'identifiant du propriétaire à supprimer.
   * @return Une ResponseEntity avec le statut HTTP indiquant le succès de l'opération.
   */
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @DeleteMapping("/{id}")
  public ResponseEntity<ProprietaireDto> delete(
      @RequestAttribute("token") String authHeader, @PathVariable Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);

    return new ResponseEntity<>(
        proprietaireService.deleteProprietaire(id),
        HttpStatus.OK
    );
  }

  /**
   * Gère la candidature des propriétaires.
   */
  @PostMapping(path = "/candidate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ProprietaireDto> candidate(
      @Valid @RequestBody ProprietaireCandidateDto proprietaireCandidateDto) {
    return new ResponseEntity<>(
        proprietaireService.candidate(proprietaireCandidateDto),
        HttpStatus.CREATED
    );
  }

  /**
   * Valide un propriétaire candidat.
   */
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @PutMapping(path = "/candidate/{id}")
  public ResponseEntity<ProprietaireDto> validateCandidate(
      @RequestAttribute("token") String authHeader, @PathVariable Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);


    return new ResponseEntity<>(
        proprietaireService.validateCandidat(id),
        HttpStatus.OK
    );
  }

  /**
   * Rejette un propriétaire candidat.
   */
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @DeleteMapping(path = "/candidate/{id}")
  public ResponseEntity<ProprietaireDto> rejectCandidate(
      @RequestAttribute("token") String authHeader, @PathVariable Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);

    return new ResponseEntity<>(
        proprietaireService.rejectCandidat(id),
        HttpStatus.OK
    );
  }

}