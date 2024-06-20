package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.controllers.interfaces.CreateMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.DeleteMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetAllMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetByIdMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.UpdateMethodInterface;
import fr.esgi.al5.tayarim.dto.logement.LogementCreationDto;
import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.dto.logement.LogementSearchDto;
import fr.esgi.al5.tayarim.dto.logement.LogementUpdateDto;
import fr.esgi.al5.tayarim.dto.logement.TypeLogementDto;
import fr.esgi.al5.tayarim.services.AuthService;
import fr.esgi.al5.tayarim.services.LogementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur responsable de la gestion des logements.
 */
@RestController
@RequestMapping("/logements")
public class LogementController implements
    CreateMethodInterface<LogementDto, LogementCreationDto>,
    GetAllMethodInterface<LogementDto>,
    GetByIdMethodInterface<LogementDto>,
    UpdateMethodInterface<LogementDto, LogementUpdateDto>,
    DeleteMethodInterface<LogementDto>,
    ControllerUtils {

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
  @Override
  public ResponseEntity<LogementDto> create(
      String authHeader,
      LogementCreationDto logementCreationDto) {

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
  @Override
  public ResponseEntity<List<LogementDto>> getAll(
      String authHeader) {
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
  @Override
  public ResponseEntity<LogementDto> getById(
      String authHeader,
      Long id) {
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

  @Override
  public ResponseEntity<LogementDto> update(
      String authHeader, Long id,
      LogementUpdateDto logementUpdateDto) {
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
  @Override
  public ResponseEntity<LogementDto> delete(
      String authHeader, Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);

    return new ResponseEntity<>(
        logementService.deleteLogement(id),
        HttpStatus.OK
    );
  }

  /**
   * Récupère tous les type de logements.
   *
   * @return Une ResponseEntity avec le statut HTTP indiquant le succès de l'opération.
   */
  @GetMapping("/types")
  public ResponseEntity<List<TypeLogementDto>> getAllTypeLogement() {

    return new ResponseEntity<>(
        logementService.getAllTypeLogement(),
        HttpStatus.OK
    );
  }

  /**
   * Récupère les logements pour la recherche basé sur certain critères.
   */
  @GetMapping("/search")
  public ResponseEntity<List<LogementDto>> search(
      @Valid @RequestBody LogementSearchDto logementSearchDto) {
    return new ResponseEntity<>(
        logementService.search(logementSearchDto),
        HttpStatus.OK
    );
  }

  /**
   * Récupère les dates occupées d'un logement.
   */
  @GetMapping("/dates/{id}")
  public ResponseEntity<List<String>> getOccupiedDates(@PathVariable Long id) {
    return new ResponseEntity<>(
        logementService.getOccupiedDates(id),
        HttpStatus.OK
    );
  }

}
