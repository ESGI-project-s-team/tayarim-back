package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.controllers.interfaces.CreateMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetAllMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetByIdMethodInterface;
import fr.esgi.al5.tayarim.dto.facture.FactureCreationDto;
import fr.esgi.al5.tayarim.dto.facture.FactureDto;
import fr.esgi.al5.tayarim.services.AuthService;
import fr.esgi.al5.tayarim.services.FactureService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    GetAllMethodInterface<FactureDto> {

  private final FactureService factureService;
  private final AuthService authService;

  public FactureController(FactureService factureService, AuthService authService) {
    this.factureService = factureService;
    this.authService = authService;
  }

  @Override
  public ResponseEntity<List<FactureDto>> getAll(String authHeader) {
    UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(
        factureService.getAll(userTokenInfo.getId(), userTokenInfo.getIsAdmin()),
        HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<FactureDto> create(String authHeader,
      FactureCreationDto factureCreationDto) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);
    return new ResponseEntity<>(
        factureService.create(factureCreationDto),
        HttpStatus.CREATED
    );
  }

  @Override
  public ResponseEntity<FactureDto> getById(String authHeader, Long id) {
    UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(
        factureService.getById(Long.toString(id), userTokenInfo.getId(),
            userTokenInfo.getIsAdmin()),
        HttpStatus.OK
    );
  }
}
