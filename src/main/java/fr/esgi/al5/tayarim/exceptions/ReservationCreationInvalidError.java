package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class ReservationCreationInvalidError extends RuntimeException {

  public ReservationCreationInvalidError() {
    super("error_reservation_creation_invalid");
  }
}