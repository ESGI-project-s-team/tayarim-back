package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.controllers.interfaces.CreateMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.DeleteMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetAllMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetByIdMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.UpdateMethodInterface;
import fr.esgi.al5.tayarim.dto.depense.DepenseCreationDto;
import fr.esgi.al5.tayarim.dto.depense.DepenseDto;
import fr.esgi.al5.tayarim.dto.depense.DepenseUpdateDto;
import fr.esgi.al5.tayarim.services.AuthService;
import fr.esgi.al5.tayarim.services.DepenseService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des dépenses.
 */
@RestController
@RequestMapping("/depenses")
public class DepenseController implements
    ControllerUtils,
    CreateMethodInterface<DepenseDto, DepenseCreationDto>,
    GetAllMethodInterface<DepenseDto>,
    GetByIdMethodInterface<DepenseDto>,
    UpdateMethodInterface<DepenseDto, DepenseUpdateDto>,
    DeleteMethodInterface<DepenseDto> {

  private final DepenseService depenseService;
  private final AuthService authService;

  public DepenseController(DepenseService depenseService,
      AuthService authService) {
    this.depenseService = depenseService;
    this.authService = authService;
  }

  @Override
  public ResponseEntity<DepenseDto> create(String authHeader,
      DepenseCreationDto depenseCreationDto) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);
    return new ResponseEntity<>(
        depenseService.create(depenseCreationDto),
        HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<List<DepenseDto>> getAll(String authHeader) {
    UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(
        depenseService.getAll(userTokenInfo.getId(), userTokenInfo.getIsAdmin()),
        HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<DepenseDto> getById(String authHeader, Long id) {
    UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(
        depenseService.getById(id, userTokenInfo.getId(), userTokenInfo.getIsAdmin()),
        HttpStatus.OK
    );
  }


  @Override
  public ResponseEntity<DepenseDto> update(String authHeader, Long id,
      DepenseUpdateDto depenseUpdateDto) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);
    return new ResponseEntity<>(
        depenseService.update(depenseUpdateDto, id),
        HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<DepenseDto> delete(String authHeader, Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);
    return new ResponseEntity<>(
        depenseService.delete(id),
        HttpStatus.OK
    );
  }
}
