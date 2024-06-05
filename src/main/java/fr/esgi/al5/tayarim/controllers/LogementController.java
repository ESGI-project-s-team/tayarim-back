package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.dto.logement.LogementCreationDto;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.dto.logement.LogementUpdateDto;
import fr.esgi.al5.tayarim.dto.logement.TypeLogementDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireUpdateDto;
import fr.esgi.al5.tayarim.exceptions.AdministrateurNotFoundException;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidCreationBody;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidTypeLogement;
import fr.esgi.al5.tayarim.exceptions.LogementInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.exceptions.TokenExpireOrInvalidException;
import fr.esgi.al5.tayarim.exceptions.UnauthorizedException;
import fr.esgi.al5.tayarim.exceptions.UtilisateurNotFoundException;
import fr.esgi.al5.tayarim.services.AuthService;
import fr.esgi.al5.tayarim.services.LogementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur responsable de la gestion des logements.
 */
@RestController
@RequestMapping("/logements")
public class LogementController {

  private final LogementService logementService;
  private final AuthService authService;

  /**
   * Construit le contrôleur avec le service de logement nécessaire.
   *
   * @param logementService Le service de logement.
   * @param authService     Le service d'authentification.
   */
  public LogementController(LogementService logementService, AuthService authService) {
    this.logementService = logementService;
    this.authService = authService;
  }

  /**
   * Crée un logement.
   *
   * @param logementCreationDto Le DTO contenant les informations de creation d'un logement.
   * @return Un ResponseEntity contenant le DTO de réponse à la creation de logement.
   */
  @PostMapping("")
  public ResponseEntity<LogementDto> createLogement(
      @RequestAttribute("token") String authHeader,
      @Valid @RequestBody LogementCreationDto logementCreationDto) {

    authService.verifyToken(getTokenFromHeader(authHeader), true);
    return new ResponseEntity<>(
        logementService.createLogement(logementCreationDto),
        HttpStatus.OK
    );
  }

  /**
   * Obtient un logement par son identifiant.
   *
   * @param authHeader L'en-tête d'autorisation contenant le token JWT.
   * @return Une ResponseEntity contenant les détails du propriétaire et le statut HTTP.
   */
  @GetMapping("")
  public ResponseEntity<List<LogementDto>> getAllLogements(
      @RequestAttribute("token") String authHeader) {
    UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(
        logementService.getAllLogement(userTokenInfo.getId(), userTokenInfo.getIsAdmin()),
        HttpStatus.OK);
  }

  /**
   * Obtient un logement par son identifiant.
   *
   * @param authHeader L'en-tête d'autorisation contenant le token JWT.
   * @param id         L'identifiant du propriétaire.
   * @return Une ResponseEntity contenant les détails du propriétaire et le statut HTTP.
   */
  @GetMapping("/{id}")
  public ResponseEntity<LogementDto> getLogementsById(
      @RequestAttribute("token") String authHeader,
      @PathVariable Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(logementService.getLogementById(id), HttpStatus.OK);
  }

  /**
   * Met à jour les informations d'un logement.
   *
   * @param authHeader        L'en-tête d'autorisation contenant le token JWT.
   * @param id                L'identifiant du logement à mettre à jour.
   * @param logementUpdateDto Les données de mise à jour du logement.
   * @return Une ResponseEntity contenant le propriétaire mis à jour et le statut HTTP.
   */

  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @PutMapping("/{id}")
  public ResponseEntity<LogementDto> updateProprietaire(
      @RequestAttribute("token") String authHeader, @PathVariable Long id,
      @Valid @RequestBody LogementUpdateDto logementUpdateDto) {
    UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), true);

    return new ResponseEntity<>(
        logementService.updateLogement(id, logementUpdateDto),
        HttpStatus.OK
    );
  }

  /**
   * Supprime un logement par son identifiant.
   *
   * @param authHeader L'en-tête d'autorisation contenant le token JWT.
   * @param id         L'identifiant du logement à supprimer.
   * @return Une ResponseEntity avec le statut HTTP indiquant le succès de l'opération.
   */
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @DeleteMapping("/{id}")
  public ResponseEntity<LogementDto> deleteProprietaire(
      @RequestAttribute("token") String authHeader, @PathVariable Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);

    return new ResponseEntity<>(
        logementService.deleteLogement(id),
        HttpStatus.OK
    );
  }

  /**
   * Récupère tous les type de logements.
   *
   * @param authHeader L'en-tête d'autorisation contenant le token JWT.
   * @return Une ResponseEntity avec le statut HTTP indiquant le succès de l'opération.
   */
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @GetMapping("/types")
  public ResponseEntity<List<TypeLogementDto>> getAllTypeLogement(
      @RequestAttribute("token") String authHeader) {
    authService.verifyToken(getTokenFromHeader(authHeader), false);

    return new ResponseEntity<>(
        logementService.getAllTypeLogement(),
        HttpStatus.OK
    );
  }

  /**
   * Extrait le token JWT de l'en-tête d'autorisation.
   *
   * @param authHeader L'en-tête contenant le token.
   * @return Le token extrait.
   * @throws TokenExpireOrInvalidException Si le token est expiré ou invalide.
   */
  private String getTokenFromHeader(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new TokenExpireOrInvalidException();
    }

    return authHeader.substring(7);
  }

  /**
   * Gère les exceptions de validation des arguments de méthode.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    ArrayList<String> errors = new ArrayList<>();
    Map<String, List<String>> errorMapping = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String errorMessage = error.getDefaultMessage();
      errors.add(errorMessage);
    });

    errorMapping.put("errors", errors);

    return errorMapping;
  }

  /**
   * Gère les exceptions lorsque le body update du logement est invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({LogementInvalidUpdateBody.class})
  public Map<String, List<String>> logementInvalidUpdateBody(LogementInvalidUpdateBody ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le body update du logement est invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({LogementInvalidTypeLogement.class})
  public Map<String, List<String>> logementInvalidTypeLogement(LogementInvalidTypeLogement ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le body update du logement est invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({LogementInvalidCreationBody.class})
  public Map<String, List<String>> logementInvalidCreationBody(LogementInvalidCreationBody ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque l'utilisateur n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({UtilisateurNotFoundException.class})
  public Map<String, List<String>> utilisateurNotFoundException(UtilisateurNotFoundException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le proprietaire n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({ProprietaireNotFoundException.class})
  public Map<String, List<String>> proprietaireNotFoundException(ProprietaireNotFoundException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le logement n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({LogementNotFoundException.class})
  public Map<String, List<String>> logementNotFoundException(LogementNotFoundException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque l'administrateur n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({AdministrateurNotFoundException.class})
  public Map<String, List<String>> administrateurNotFoundException(
      AdministrateurNotFoundException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le token est expiré ou invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs.
   */
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({TokenExpireOrInvalidException.class})
  public Map<String, List<String>> tokenExpireOrInvalidException(TokenExpireOrInvalidException ex) {
    return mapException(ex);
  }

  /**
   * Crée une carte d'erreurs à partir d'une exception runtime.
   *
   * @param exception L'exception à mapper.
   * @return Une carte des erreurs associées à l'exception.
   */
  private Map<String, List<String>> mapException(RuntimeException exception) {
    ArrayList<String> errors = new ArrayList<>();
    Map<String, List<String>> errorMapping = new HashMap<>();
    errors.add(exception.getMessage());

    errorMapping.put("errors", errors);

    return errorMapping;
  }
}
