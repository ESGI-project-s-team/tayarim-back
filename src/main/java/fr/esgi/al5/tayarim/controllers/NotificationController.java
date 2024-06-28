package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.controllers.interfaces.GetAllMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetByIdMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.UpdateMethodInterface;
import fr.esgi.al5.tayarim.dto.notification.NotificationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationCreationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationUpdateDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationUpdatePaymentIntentDto;
import fr.esgi.al5.tayarim.services.AuthService;
import fr.esgi.al5.tayarim.services.NotificationService;
import fr.esgi.al5.tayarim.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des reservations.
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController implements
    ControllerUtils,
    GetAllMethodInterface<NotificationDto> {

  private final NotificationService notificationService;
  private final AuthService authService;

  public NotificationController(NotificationService notificationService,
      AuthService authService) {
    this.notificationService = notificationService;
    this.authService = authService;
  }


  @Override
  public ResponseEntity<List<NotificationDto>> getAll(String authHeader) {
    UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(
        notificationService.getAll(userTokenInfo.getId()),
        HttpStatus.OK
    );
  }

  /**
   * Marque une notification comme lue.
   */
  @PutMapping("/read/{id}")
  public ResponseEntity<NotificationDto> read(
      @RequestAttribute(name = "token", required = false) String authHeader,
      @PathVariable Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(
        notificationService.read(id),
        HttpStatus.OK
    );
  }
}
