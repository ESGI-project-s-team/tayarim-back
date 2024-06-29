package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class ReservationPeopleCapacityError extends RuntimeException {

  public ReservationPeopleCapacityError() {
    super("error_reservation_people_capacity_invalid");
  }
}