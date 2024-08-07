package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.reservation.ReservationCreationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationDto;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Classe pour mapper les entités Reservation en DTOs Reservation et vice versa.
 */
@Component
public class ReservationMapper {

  /**
   * Convertit une entité Reservation en un DTO Reservation.
   *
   * @param reservationCreationDto Le Dto Reservation à convertir.
   * @param idCommande             L'identifiant de la commande associée à la réservation.
   * @param dateArrivee            La date d'arrivée.
   * @param dateDepart             La date de départ.
   * @param logement               Le logement associé à la réservation.
   * @param dateReservation        La date de la réservation.
   * @return Le DTO Reservation correspondant.
   */
  public static Reservation creationDtoToEntity(
      @NonNull ReservationCreationDto reservationCreationDto, @NonNull String idCommande,
      @NonNull String status,
      @NonNull LocalDate dateArrivee, @NonNull LocalDate dateDepart,
      @NonNull Logement logement, @NonNull LocalDateTime dateReservation, String paymentIntent
  ) {

    return new Reservation(
        idCommande,
        status,
        reservationCreationDto.getEmail(),
        reservationCreationDto.getNumTel(),
        reservationCreationDto.getNom(),
        reservationCreationDto.getPrenom(),
        reservationCreationDto.getNbPersonnes(),
        reservationCreationDto.getMontant(),
        logement.getDefaultCheckIn(),
        logement.getDefaultCheckOut(),
        dateArrivee,
        dateDepart,
        logement,
        dateReservation,
        paymentIntent,
        reservationCreationDto.getLang()
    );
  }

  /**
   * Convertit une entité Reservation en un DTO Reservation.
   *
   * @param reservation L'entité Reservation à convertir.
   * @return Le DTO Reservation correspondant.
   */
  public static ReservationDto entityToDto(@NonNull Reservation reservation) {
    return new ReservationDto(
        reservation.getId(),
        reservation.getIdCommande(),
        reservation.getStatut(),
        reservation.getEmail(),
        reservation.getNumTel(),
        reservation.getNom(),
        reservation.getPrenom(),
        reservation.getNbPersonnes(),
        reservation.getMontant(),
        reservation.getCheckIn().toString().substring(0, 5),
        reservation.getCheckOut().toString().substring(0, 5),
        reservation.getDateArrivee().toString(),
        reservation.getDateDepart().toString(),
        reservation.getLogement().getId(),
        reservation.getLogement().getTitre(),
        reservation.getDateReservation().toString(),
        reservation.getPaymentIntent(),
        reservation.getLanguage()
    );
  }

  /**
   * Convertit une liste d'entités Reservation en une liste de DTOs Reservation.
   *
   * @param reservations La liste des Reservation à convertir.
   * @return Une liste de DTOs Reservation.
   */
  public static List<ReservationDto> entityListToDtoList(@NonNull List<Reservation> reservations) {
    ArrayList<ReservationDto> reservationDtos = new ArrayList<>();
    for (Reservation reservation : reservations) {
      reservationDtos.add(entityToDto(reservation));
    }
    return reservationDtos.stream().toList();
  }

}
