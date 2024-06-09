package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.dto.regleslogement.ReglesLogementDto;
import fr.esgi.al5.tayarim.services.AuthService;
import fr.esgi.al5.tayarim.services.ReglesLogementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des règles de logement.
 */
@RestController
@RequestMapping("/reglesLogement")
public class ReglesLogementController implements
    ControllerUtils {

  private final ReglesLogementService reglesLogementService;
  private final AuthService authService;

  public ReglesLogementController(ReglesLogementService reglesLogementService,
      AuthService authService) {
    this.reglesLogementService = reglesLogementService;
    this.authService = authService;
  }

  /**
   * Récupère toutes les règles de logements.
   *
   * @return {@link ReglesLogementDto}
   */
  @Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @GetMapping("")
  public ResponseEntity<List<ReglesLogementDto>> getAll() {
    return new ResponseEntity<>(
        reglesLogementService.getAllReglesLogements(),
        HttpStatus.OK
    );
  }

}
