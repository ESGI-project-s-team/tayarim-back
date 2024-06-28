package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un logement spécifié n'est pas trouvé dans le système.
 */
public class ReservationNotFoundException extends RuntimeException {

  public ReservationNotFoundException() {
    super("error_reservation_notExist");
  }
}