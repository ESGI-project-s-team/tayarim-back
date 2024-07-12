package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.dto.amenagement.AmenagementDto;
import fr.esgi.al5.tayarim.services.AmenagementService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des amenagements.
 */
@RestController
@RequestMapping("/amenagements")
public class AmenagementController implements
    ControllerUtils {

  private final AmenagementService amenagementService;

  public AmenagementController(AmenagementService amenagementService) {
    this.amenagementService = amenagementService;
  }

  /**
   * Récupère toutes les amenagements.
   *
   * @return {@link AmenagementDto}
   */
  @GetMapping("")
  public ResponseEntity<List<AmenagementDto>> getAll() {
    return new ResponseEntity<>(
        amenagementService.getAllAmenagements(),
        HttpStatus.OK
    );
  }



}
