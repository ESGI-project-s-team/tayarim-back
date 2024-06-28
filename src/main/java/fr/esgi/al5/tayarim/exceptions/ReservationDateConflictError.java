package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class ReservationDateConflictError extends RuntimeException {

  public ReservationDateConflictError() {
    super("error_reservation_date_conflict");
  }
}