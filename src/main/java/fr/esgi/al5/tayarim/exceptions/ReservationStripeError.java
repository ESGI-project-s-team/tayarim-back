package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class ReservationStripeError extends RuntimeException {

  public ReservationStripeError() {
    super("error_reservation_stripe");
  }
}