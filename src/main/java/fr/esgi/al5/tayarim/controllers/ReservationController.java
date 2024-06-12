package fr.esgi.al5.tayarim.controllers;

import fr.esgi.al5.tayarim.controllers.interfaces.ControllerUtils;
import fr.esgi.al5.tayarim.controllers.interfaces.CreateMethodInterface;
import fr.esgi.al5.tayarim.controllers.interfaces.GetAllMethodInterface;
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
    GetAllMethodInterface<ReservationDto>
    {

  private final ReservationService reservationService;
  private final AuthService authService;

  public ReservationController(ReservationService reservationService,
      AuthService authService) {
    this.reservationService = reservationService;
    this.authService = authService;
  }

  @Override
  public ResponseEntity<ReservationDto> create(String authHeader, @Valid ReservationCreationDto reservationCreationDto) {
    authService.verifyToken(authHeader, false);

    return new ResponseEntity<>(
      reservationService.createReservation(reservationCreationDto),
      HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<ReservationDto> update(String authHeader, Long id, @Valid ReservationUpdateDto reservationUpdateDto) {
    authService.verifyToken(authHeader, true);

    return new ResponseEntity<>(
      reservationService.updateReservation(id, reservationUpdateDto),
      HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<List<ReservationDto>> getAll(String authHeader) {
    authService.verifyToken(authHeader, false);

    return new ResponseEntity<>(
      reservationService.getAllReservation(),
      HttpStatus.OK
    );
  }

}
