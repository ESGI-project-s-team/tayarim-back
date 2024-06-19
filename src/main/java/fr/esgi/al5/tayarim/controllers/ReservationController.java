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

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour la gestion des reservations.
 */
@RestController
@RequestMapping("/reservations")
public class ReservationController implements
    ControllerUtils,
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

  /**
   * Crée une nouvelle réservation.
   *
   * @param reservationCreationDto Dto de création de réservation.
   */
  @PostMapping("")
  public ResponseEntity<ReservationDto> create(
      @RequestAttribute(name = "token", required = false) String authHeader,
      @Valid @RequestBody ReservationCreationDto reservationCreationDto) {

    boolean isAdmin = false;

    if (authHeader != null) {
      isAdmin = authService.verifyToken(getTokenFromHeader(authHeader), false).getIsAdmin();
    }

    return new ResponseEntity<>(
        reservationService.createReservation(reservationCreationDto, isAdmin),
        HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<ReservationDto> update(String authHeader, Long id,
      @Valid ReservationUpdateDto reservationUpdateDto) {
    UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), true);

    return new ResponseEntity<>(
        reservationService.updateReservation(id, reservationUpdateDto, userTokenInfo.getIsAdmin()),
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
   * Récupère les règles d'un logement.
   *
   * @param id Id du logement.
   */
  //@Operation(summary = "Authenticate user", security = @SecurityRequirement(name = "bearer-key"))
  @PutMapping("/cancel/{id}")
  public ResponseEntity<ReservationDto> cancel(/*@RequestAttribute("token") String authHeader,*/
      @PathVariable Long id) {
    //UserTokenInfo userTokenInfo = authService.verifyToken(getTokenFromHeader(authHeader), false);
    return new ResponseEntity<>(
        reservationService.cancel(id/*, userTokenInfo.getIsAdmin(), userTokenInfo.getId()*/),
        HttpStatus.OK
    );
  }
}
