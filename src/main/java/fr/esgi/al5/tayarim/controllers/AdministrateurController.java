package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.dto.proprietaire.AdministrateurDto;
import fr.esgi.al5.tayarim.dto.proprietaire.AdministrateurUpdateDto;
import fr.esgi.al5.tayarim.exceptions.AdministrateurEmailAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.AdministrateurInvalidUpdateBody;
import fr.esgi.al5.tayarim.exceptions.AdministrateurNotFoundException;
import fr.esgi.al5.tayarim.exceptions.AdministrateurNumTelAlreadyExistException;
import fr.esgi.al5.tayarim.exceptions.PasswordHashNotPossibleException;
import fr.esgi.al5.tayarim.exceptions.TokenExpireOrInvalidException;
import fr.esgi.al5.tayarim.exceptions.UnauthorizedException;
import fr.esgi.al5.tayarim.services.AdministrateurService;
import fr.esgi.al5.tayarim.services.AuthService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur gérant les opérations administratives sur les comptes des administrateurs.
 */
@RestController
@RequestMapping("/admin")
public class AdministrateurController {

  private final AdministrateurService administrateurService;
  private final AuthService authService;

  /**
   * Constructeur pour initialiser les services utilisés par ce contrôleur.
   *
   * @param administrateurService Le service pour la gestion des administrateurs.
   * @param authService           Le service pour l'authentification et la vérification des tokens.
   */
  public AdministrateurController(AdministrateurService administrateurService,
      AuthService authService) {
    this.administrateurService = administrateurService;
    this.authService = authService;
  }

  /**
   * Récupère la liste de tous les administrateurs.
   *
   * @param authHeader Le token d'authentification fourni dans l'en-tête de la requête.
   * @return Une réponse contenant la liste des administrateurs et le statut HTTP.
   */
  @GetMapping("")
  public ResponseEntity<List<AdministrateurDto>> getAdministrateur(
      @RequestHeader("Authorization") String authHeader) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);

    return new ResponseEntity<>(
        administrateurService.getAdministrateur(),
        HttpStatus.OK
    );
  }

  /**
   * Récupère un administrateur par son identifiant.
   *
   * @param authHeader Le token d'authentification fourni dans l'en-tête de la requête.
   * @param id         L'identifiant de l'administrateur à récupérer.
   * @return Une réponse contenant les détails de l'administrateur et le statut HTTP.
   */
  @GetMapping("/{id}")
  public ResponseEntity<AdministrateurDto> getAdministrateur(
      @RequestHeader("Authorization") String authHeader, @PathVariable Long id) {

    authService.verifyToken(getTokenFromHeader(authHeader), true);

    return new ResponseEntity<>(
        administrateurService.getAdministrateurById(id),
        HttpStatus.OK
    );
  }

  /**
   * Met à jour les informations d'un administrateur.
   *
   * @param authHeader              Le token d'authentification fourni dans l'en-tête de la
   *                                requête.
   * @param id                      L'identifiant de l'administrateur à mettre à jour.
   * @param administrateurUpdateDto Les nouvelles informations de l'administrateur.
   * @return Une réponse contenant l'administrateur mis à jour et le statut HTTP.
   */
  @PutMapping("/{id}")
  public ResponseEntity<AdministrateurDto> updateAdministrateur(
      @RequestHeader("Authorization") String authHeader, @PathVariable Long id,
      @RequestBody AdministrateurUpdateDto administrateurUpdateDto) {
    Long idToken = authService.verifyToken(getTokenFromHeader(authHeader), true).getKey();

    if (!idToken.equals(id)) {
      throw new UnauthorizedException();
    }

    return new ResponseEntity<>(
        administrateurService.updateAdministrateur(id, administrateurUpdateDto),
        HttpStatus.OK
    );
  }

  /**
   * Supprime un administrateur par son identifiant.
   *
   * @param authHeader Le token d'authentification fourni dans l'en-tête de la requête.
   * @param id         L'identifiant de l'administrateur à supprimer.
   * @return Une réponse avec le statut HTTP indiquant le succès ou l'échec de l'opération.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<AdministrateurDto> deleteAdministrateur(
      @RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
    Long idToken = authService.verifyToken(getTokenFromHeader(authHeader), true).getKey();
    if (!idToken.equals(id)) {
      throw new UnauthorizedException();
    }

    return new ResponseEntity<>(
        administrateurService.deleteAdministrateur(id),
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
   * Gère les exceptions de validation des arguments de méthode lors de requêtes HTTP.
   *
   * @param ex L'exception capturée contenant les erreurs de validation.
   * @return Une carte des erreurs listant les messages de validation échoués.
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
   * Gère les exceptions lorsque le corps de mise à jour de l'administrateur est invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs avec le message approprié.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(AdministrateurInvalidUpdateBody.class)
  public Map<String, List<String>> administrateurInvalidUpdateBody(
      AdministrateurInvalidUpdateBody ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque l'email spécifié pour l'administrateur existe déjà.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs avec le message approprié.
   */
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler({AdministrateurEmailAlreadyExistException.class})
  public Map<String, List<String>> administrateurEmailAlreadyExistException(
      AdministrateurEmailAlreadyExistException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le numéro de téléphone spécifié pour l'administrateur existe déjà.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs avec le message approprié.
   */
  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler({AdministrateurNumTelAlreadyExistException.class})
  public Map<String, List<String>> administrateurNumTelAlreadyExistException(
      AdministrateurNumTelAlreadyExistException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque l'administrateur recherché n'est pas trouvé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs avec le message approprié.
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({AdministrateurNotFoundException.class})
  public Map<String, List<String>> administrateurNotFoundException(
      AdministrateurNotFoundException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsqu'il n'est pas possible de hasher le mot de passe.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs avec le message approprié.
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({PasswordHashNotPossibleException.class})
  public Map<String, List<String>> passwordHashNotPossibleException(
      PasswordHashNotPossibleException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions lorsque le token est expiré ou invalide.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs avec le message approprié.
   */
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler({TokenExpireOrInvalidException.class})
  public Map<String, List<String>> tokenExpireOrInvalidException(TokenExpireOrInvalidException ex) {
    return mapException(ex);
  }

  /**
   * Gère les exceptions d'accès non autorisé.
   *
   * @param ex L'exception capturée.
   * @return Une carte des erreurs avec le message approprié.
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
