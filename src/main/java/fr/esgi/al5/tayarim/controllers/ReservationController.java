package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.auth.UserTokenInfo;
import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.controllers.interfaces.CreateMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetAllMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetByIdMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.UpdateMethodInterface;
import fr.esgi.al5.tayarim.dto.regleslogement.ReglesLogementDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationCreationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationUpdateDto;
import fr.esgi.al5.tayarim.services.AuthService;
import fr.esgi.al5.tayarim.services.ReglesLogementService;
import fr.esgi.al5.tayarim.services.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour la gestion des reservations.
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController implements
    ControllerUtils,
    CreateMethodInterface<ReservationDto, ReservationCreationDto>,
    UpdateMethodInterface<ReservationDto, ReservationUpdateDto>,
    GetAllMethodInterface<ReservationDto>,
    GetByIdMethodInterface<ReservationDto> {

  private final ReservationService reservationService;
  private final AuthService authService;

  public ReservationController(ReservationService reservationService,
      AuthService authService) {
    this.reservationService = reservationService;
    this.authService = authService;
  }

  @Override
  public ResponseEntity<ReservationDto> create(String authHeader,
      @Valid ReservationCreationDto reservationCreationDto) {
    authService.verifyToken(getTokenFromHeader(authHeader), false);

    return new ResponseEntity<>(
        reservationService.createReservation(reservationCreationDto),
        HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<ReservationDto> update(String authHeader, Long id,
      @Valid ReservationUpdateDto reservationUpdateDto) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);

    return new ResponseEntity<>(
        reservationService.updateReservation(id, reservationUpdateDto),
        HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<List<ReservationDto>> getAll(String authHeader) {
    UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(
        reservationService.getAllReservation(userTokenInfo.getIsAdmin(), userTokenInfo.getId()),
        HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<ReservationDto> getById(String authHeader, Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), false);

    return new ResponseEntity<>(
        reservationService.getReservationById(id),
        HttpStatus.OK
    );
  }

  /**
   * Mets à jour le statut de la réservation.
   *
   * @param authHeader Le token d'authentification.
   * @param id         L'identifiant de la réservation.
   * @return Les règles de logement.
   */
  @PutMapping("/nextStep/{id}")
  public ResponseEntity<ReservationDto> nextStep(@RequestAttribute("token") String authHeader,
      @PathVariable Long id) {
    authService.verifyToken(getTokenFromHeader(authHeader), true);

    return new ResponseEntity<>(
        reservationService.nextStep(id),
        HttpStatus.OK
    );
  }
}
