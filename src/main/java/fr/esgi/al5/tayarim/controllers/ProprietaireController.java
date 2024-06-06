package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireCreationDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireUpdateDto;
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
import java.util.Map.Entry;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des propriétaires.
 */
@RestController
@RequestMapping("/proprietaires")
public class ProprietaireController {

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
  public ResponseEntity<ProprietaireDto> creerProprietaire(
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
  public ResponseEntity<List<ProprietaireDto>> getProprietaire(
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
  public ResponseEntity<ProprietaireDto> getProprietaire(
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
  public ResponseEntity<ProprietaireDto> updateProprietaire(
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
  public ResponseEntity<ProprietaireDto> deleteProprietaire(
      @RequestAttribute("token") String authHeader, @PathVariable Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);

    return new ResponseEntity<>(
        proprietaireService.deleteProprietaire(id),
        HttpStatus.OK
    );
  }

  private String getTokenFromHeader(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new TokenExpireOrInvalidException();
    }

    return authHeader.substring(7);
  }

  /**
   * Gère les exceptions liées aux arguments de méthode non valides en retournant un message
   * d'erreur approprié.
   *
   * @param ex L'exception capturée de type MethodArgumentNotValidException.
   * @return Une carte associant le type d'erreur à la liste de messages d'erreur.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    ArrayList<String> errors = new ArrayList<>();
    Map<String, List<String>> errorMapping = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String errorMessage = error.getDefaultMessage();
      if (!errors.contains(errorMessage)) {
        errors.add(errorMessage);
      }
    });

    errorMapping.put("errors", errors);

    return errorMapping;
  }

  /**
   * Gère les cas où le corps de la mise à jour d'un propriétaire est invalide.
   *
   * @param ex L'exception capturée indiquant un corps invalide pour la mise à jour.
   * @return Une carte des erreurs liées à la mise à jour.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ProprietaireInvalidUpdateBody.class)
  public Map<String, List<String>> proprietaireInvalidUpdateBody(ProprietaireInvalidUpdateBody ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions liées à l'existence préalable de l'email du propriétaire.
   *
   * @param ex L'exception capturée lorsque l'email donné existe déjà.
   * @return Une carte des conflits liés à l'email existant.
   */
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler({ProprietaireEmailAlreadyExistException.class})
  public Map<String, List<String>> proprietaireEmailAlreadyExistException(
      ProprietaireEmailAlreadyExistException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions liées à l'existence préalable du numéro de téléphone du propriétaire.
   *
   * @param ex L'exception capturée lorsque le numéro de téléphone donné existe déjà.
   * @return Une carte des conflits liés au numéro de téléphone existant.
   */
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler({ProprietaireNumTelAlreadyExistException.class})
  public Map<String, List<String>> proprietaireNumTelAlreadyExistException(
      ProprietaireNumTelAlreadyExistException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsqu'un propriétaire spécifié n'est pas trouvé.
   *
   * @param ex L'exception capturée lorsque le propriétaire n'est pas trouvé.
   * @return Une carte des erreurs signalant l'absence du propriétaire.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({ProprietaireNotFoundException.class})
  public Map<String, List<String>> proprietaireNotFoundException(ProprietaireNotFoundException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions liées à l'échec de hashage de mot de passe.
   *
   * @param ex L'exception capturée indiquant un problème lors du hashage du mot de passe.
   * @return Une carte des erreurs internes liées au hashage de mot de passe.
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({PasswordHashNotPossibleException.class})
  public Map<String, List<String>> passwordHashNotPossibleException(
      PasswordHashNotPossibleException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions liées à un token JWT expiré ou invalide.
   *
   * @param ex L'exception capturée indiquant un problème avec le token JWT.
   * @return Une carte des erreurs d'autorisation.
   */
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({TokenExpireOrInvalidException.class})
  public Map<String, List<String>> tokenExpireOrInvalidException(TokenExpireOrInvalidException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions d'accès non autorisé lorsque les conditions d'autorisation ne sont pas
   * remplies.
   *
   * @param ex L'exception capturée indiquant un accès non autorisé.
   * @return Une carte des erreurs d'autorisation.
   */
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({UnauthorizedException.class})
  public Map<String, List<String>> unauthorizedException(UnauthorizedException ex) {
    return mapException(ex);
  }

  private Map<String, List<String>> mapException(RuntimeException exception) {
    ArrayList<String> errors = new ArrayList<>();
    Map<String, List<String>> errorMapping = new HashMap<>();
    errors.add(exception.getMessage());

    errorMapping.put("errors", errors);

    return errorMapping;
  }
}