package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.dto.auth.AuthLoginDto;
import fr.esgi.al5.tayarim.dto.auth.AuthLoginResponseDto;
import fr.esgi.al5.tayarim.dto.auth.AuthRefreshDto;
import fr.esgi.al5.tayarim.dto.auth.AuthRefreshResponseDto;
import fr.esgi.al5.tayarim.dto.auth.AuthResponseDto;
import fr.esgi.al5.tayarim.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur responsable de la gestion de l'authentification des utilisateurs.
 */
@RestController
@RequestMapping("/auth")
public class AuthController implements ControllerUtils {

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
   * @param authLoginDto Le DTO contenant les informations de connexion de l'utilisateur.
   * @return Un ResponseEntity contenant le DTO de réponse d'authentification et le statut HTTP.
   */
  @PostMapping("/login")
  public ResponseEntity<AuthLoginResponseDto> login(@Valid @RequestBody AuthLoginDto authLoginDto) {
    ResponseEntity<AuthLoginResponseDto> response = new ResponseEntity<>(
        authService.login(authLoginDto.getEmail(), authLoginDto.getMotDePasse()),
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
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @GetMapping("")
  public ResponseEntity<AuthResponseDto> auth(
      @RequestAttribute("token") String authHeader) {

    String jwtToken = getTokenFromHeader(authHeader);

    return new ResponseEntity<>(
        authService.auth(jwtToken),
        HttpStatus.OK
    );
  }

  /**
   * Actualise le token de l'utilisateur à partir de son token JWT et du refresh token.
   *
   * @param authRefreshDto Corps de la requêtes contenant le refresh token.
   * @return Un ResponseEntity contenant le DTO de réponse de refresh et le statut HTTP.
   */
  @PostMapping("/refresh")
  public ResponseEntity<AuthRefreshResponseDto> refresh(
      @Valid @RequestBody AuthRefreshDto authRefreshDto) {

    return new ResponseEntity<>(
        authService.refresh(authRefreshDto.getRefreshToken()),
        HttpStatus.OK
    );
  }

  /**
   * Déconnecte un utilisateur en invalidant son token JWT.
   *
   * @param authHeader L'en-tête d'autorisation contenant le token JWT.
   * @return Un ResponseEntity avec le statut HTTP indiquant le succès de l'opération.
   */
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @GetMapping("/logout")
  public ResponseEntity<Void> logout(@RequestAttribute("token") String authHeader) {

    String jwtToken = getTokenFromHeader(authHeader);
    authService.logout(jwtToken);

    return new ResponseEntity<>(
        HttpStatus.OK
    );
  }

}
