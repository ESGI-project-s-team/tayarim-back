package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class ReservationDateInvalideError extends RuntimeException {

  public ReservationDateInvalideError() {
    super("error_reservation_date_invalide");
  }
}