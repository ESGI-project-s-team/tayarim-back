package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class ReservationStatusUpdateError extends RuntimeException {

  public ReservationStatusUpdateError() {
    super("error_reservation_status_update");
  }
}