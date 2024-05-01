package fr.esgi.al5_2.Tayarim.controllers;

import fr.esgi.al5_2.Tayarim.dto.auth.AuthLoginDTO;
import fr.esgi.al5_2.Tayarim.dto.auth.AuthLoginResponseDTO;
import fr.esgi.al5_2.Tayarim.exceptions.*;
import fr.esgi.al5_2.Tayarim.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur responsable de la gestion de l'authentification des utilisateurs.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  /**
   * Construit le contrôleur avec le service d'authentification nécessaire.
   *
   * @param authProprietaire Le service d'authentification.
   */
  public AuthController(AuthService authProprietaire) {
    this.authService = authProprietaire;
  }

  /**
   * Authentifie un utilisateur et renvoie un token JWT.
   *
   * @param authLoginDTO Le DTO contenant les informations de connexion de l'utilisateur.
   * @return Un ResponseEntity contenant le DTO de réponse d'authentification et le statut HTTP.
   */
  @PostMapping("/login")
  public ResponseEntity<AuthLoginResponseDTO> login(@Valid @RequestBody AuthLoginDTO authLoginDTO) {
    ResponseEntity<AuthLoginResponseDTO> response = new ResponseEntity<>(
        authService.login(authLoginDTO.getEmail(), authLoginDTO.getMotDePasse()),
        HttpStatus.OK
    );

    return response;
  }

  /**
   * Authentifie un utilisateur à partir d'un token JWT fourni.
   *
   * @param authHeader L'en-tête d'autorisation contenant le token JWT.
   * @return Un ResponseEntity contenant le DTO de réponse d'authentification et le statut HTTP.
   */
  @GetMapping("")
  public ResponseEntity<AuthLoginResponseDTO> auth(
      @RequestHeader("Authorization") String authHeader) {

    String jwtToken = getTokenFromHeader(authHeader);

    return new ResponseEntity<>(
        authService.auth(jwtToken),
        HttpStatus.OK
    );
  }

  /**
   * Déconnecte un utilisateur en invalidant son token JWT.
   *
   * @param authHeader L'en-tête d'autorisation contenant le token JWT.
   * @return Un ResponseEntity avec le statut HTTP indiquant le succès de l'opération.
   */
  @GetMapping("/logout")
  public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {

    String jwtToken = getTokenFromHeader(authHeader);
    authService.logout(jwtToken);

    return new ResponseEntity<>(
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
