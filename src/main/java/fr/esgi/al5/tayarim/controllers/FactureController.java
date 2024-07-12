package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.controllers.interfaces.CreateMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.DeleteMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetAllMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetByIdMethodInterface;
import fr.esgi.al5.tayarim.dto.facture.FactureCreationDto;
import fr.esgi.al5.tayarim.dto.facture.FactureDto;
import fr.esgi.al5.tayarim.services.AuthService;
import fr.esgi.al5.tayarim.services.FactureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des facture.
 */
@RestController
@RequestMapping("/factures")
public class FactureController implements
    ControllerUtils,
    CreateMethodInterface<FactureDto, FactureCreationDto>,
    GetByIdMethodInterface<FactureDto>,
    GetAllMethodInterface<FactureDto>,
    DeleteMethodInterface<FactureDto> {

  private final FactureService factureService;
  private final AuthService authService;

  public FactureController(FactureService factureService, AuthService authService) {
    this.factureService = factureService;
    this.authService = authService;
  }

  /**
   * Méthode pour récupérer toutes les factures.
   */
  @Override
  public ResponseEntity<List<FactureDto>> getAll(String authHeader) {
    UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(
        factureService.getAll(userTokenInfo.getId(), userTokenInfo.getIsAdmin()),
        HttpStatus.OK
    );
  }

  /**
   * Méthode pour creer une facture et l'ajouter à la base de données.
   */
  @Override
  public ResponseEntity<FactureDto> create(String authHeader,
      FactureCreationDto factureCreationDto) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);
    return new ResponseEntity<>(
        factureService.create(factureCreationDto),
        HttpStatus.CREATED
    );
  }

  /**
   * Méthode pour récupérer une facture par son id.
   */
  @Override
  public ResponseEntity<FactureDto> getById(String authHeader, Long id) {
    UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(
        factureService.getById(Long.toString(id), userTokenInfo.getId(),
            userTokenInfo.getIsAdmin()),
        HttpStatus.OK
    );
  }

  /**
   * Méthode pour envoyer une facture par mail et une notification.
   */
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @PutMapping("/send/{id}")
  public ResponseEntity<FactureDto> sendFacture(@RequestAttribute("token") String authHeader,
      @PathVariable Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);
    return new ResponseEntity<>(
        factureService.sendFacture(id),
        HttpStatus.OK
    );
  }

  /**
   * Méthode pour supprimer une facture.
   */

  @Override
  public ResponseEntity<FactureDto> delete(String authHeader, Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);
    return new ResponseEntity<>(
        factureService.delete(id),
        HttpStatus.OK
    );
  }

  /**
   * Génère lien facture.
   */
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @GetMapping("/link/{id}")
  public ResponseEntity<Map<String, String>> linkFacture(
      @RequestAttribute("token") String authHeader,
      @PathVariable Long id) {
    UserTokenInfo userTokenInfo =
        authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(
        Map.of("url",
            factureService.linkFacture(id, userTokenInfo.getId(), userTokenInfo.getIsAdmin())),
        HttpStatus.OK
    );
  }
}
