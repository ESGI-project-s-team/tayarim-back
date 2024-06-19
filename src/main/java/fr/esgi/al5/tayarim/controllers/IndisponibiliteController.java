package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.controllers.interfaces.CreateMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetAllMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetByIdMethodInterface;
import fr.esgi.al5.tayarim.dto.indisponibilite.IndisponibiliteCreationDto;
import fr.esgi.al5.tayarim.dto.indisponibilite.IndisponibiliteDto;
import fr.esgi.al5.tayarim.services.AuthService;
import fr.esgi.al5.tayarim.services.IndisponibiliteService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des reservations.
 */
@RestController
@RequestMapping("/indisponibilites")
public class IndisponibiliteController implements
    ControllerUtils,
    CreateMethodInterface<IndisponibiliteDto, IndisponibiliteCreationDto>,
    GetAllMethodInterface<IndisponibiliteDto>,
    GetByIdMethodInterface<IndisponibiliteDto> {

  private final IndisponibiliteService indisponibiliteService;
  private final AuthService authService;

  public IndisponibiliteController(IndisponibiliteService indisponibiliteService,
      AuthService authService) {
    this.indisponibiliteService = indisponibiliteService;
    this.authService = authService;
  }


  @Override
  public ResponseEntity<IndisponibiliteDto> create(String authHeader,
      IndisponibiliteCreationDto indisponibiliteCreationDto) {
    UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(
        indisponibiliteService.create(indisponibiliteCreationDto, userTokenInfo.getId(),
            userTokenInfo.getIsAdmin()),
        HttpStatus.OK
    );
  }


  @Override
  public ResponseEntity<List<IndisponibiliteDto>> getAll(String authHeader) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);
    return new ResponseEntity<>(
        indisponibiliteService.getAll(),
        HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<IndisponibiliteDto> getById(String authHeader, Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);
    return new ResponseEntity<>(
        indisponibiliteService.getById(id),
        HttpStatus.OK
    );
  }
}
