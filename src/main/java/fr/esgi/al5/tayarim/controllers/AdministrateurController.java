package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.controllers.interfaces.DeleteMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetAllMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetByIdMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.UpdateMethodInterface;
import fr.esgi.al5.tayarim.dto.proprietaire.AdministrateurDto;
import fr.esgi.al5.tayarim.dto.proprietaire.AdministrateurUpdateDto;
import fr.esgi.al5.tayarim.exceptions.UnauthorizedException;
import fr.esgi.al5.tayarim.exceptions.UnsupportedMethodPathException;
import fr.esgi.al5.tayarim.services.AdministrateurService;
import fr.esgi.al5.tayarim.services.AuthService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur gérant les opérations administratives sur les comptes des administrateurs.
 */
@RestController
@RequestMapping("/admin")
public class AdministrateurController implements GetAllMethodInterface<AdministrateurDto>,
    GetByIdMethodInterface<AdministrateurDto>,
    UpdateMethodInterface<AdministrateurDto, AdministrateurUpdateDto>,
    DeleteMethodInterface<AdministrateurDto>,
    ControllerUtils {

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
  public ResponseEntity<List<AdministrateurDto>> getAll(
      String authHeader) {
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
  public ResponseEntity<AdministrateurDto> getById(
      String authHeader, Long id) {

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
  public ResponseEntity<AdministrateurDto> update(
      String authHeader, Long id,
      AdministrateurUpdateDto administrateurUpdateDto) {
    Long idToken = authService.verifyToken(getTokenFromHeader(authHeader), true).getId();

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
  public ResponseEntity<AdministrateurDto> delete(
      String authHeader, Long id) {
    Long idToken = authService.verifyToken(getTokenFromHeader(authHeader), true).getId();
    if (!idToken.equals(id)) {
      throw new UnauthorizedException();
    }

    return new ResponseEntity<>(
        administrateurService.deleteAdministrateur(id),
        HttpStatus.OK
    );
  }
}
