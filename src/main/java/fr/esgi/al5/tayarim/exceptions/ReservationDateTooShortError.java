package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class ReservationDateTooShortError extends RuntimeException {

  public ReservationDateTooShortError() {
    super("error_reservation_date_too_short");
  }
}