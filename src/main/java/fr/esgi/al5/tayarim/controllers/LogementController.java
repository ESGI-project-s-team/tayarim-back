package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.dto.auth.AuthLoginDto;
import fr.esgi.al5.tayarim.dto.auth.AuthLoginResponseDto;
import fr.esgi.al5.tayarim.dto.auth.AuthRefreshDto;
import fr.esgi.al5.tayarim.dto.auth.AuthRefreshResponseDto;
import fr.esgi.al5.tayarim.dto.auth.AuthResponseDto;
import fr.esgi.al5.tayarim.dto.logement.LogementCreationDto;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.exceptions.AdministrateurNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.exceptions.TokenExpireOrInvalidException;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

  /**
   * Construit le contrôleur avec le service de logement nécessaire.
   *
   * @param logementService Le service de logement.
   */
  public LogementController(LogementService logementService) {
    this.logementService = logementService;
  }

  /**
   * Crée un logement.
   *
   * @param logementCreationDto Le DTO contenant les informations de creation d'un logement.
   * @return Un ResponseEntity contenant le DTO de réponse à la creation de logement.
   */
  @PostMapping("")
  public ResponseEntity<LogementDto> createLogement(@Valid @RequestBody LogementCreationDto logementCreationDto) {

    return new ResponseEntity<>(
        logementService.createLogement(logementCreationDto),
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
