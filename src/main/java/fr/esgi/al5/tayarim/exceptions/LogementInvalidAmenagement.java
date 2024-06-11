package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsque la mise à jour d'un propriétaire contient des données invalides.
 */
public class LogementInvalidAmenagement extends RuntimeException {

  public LogementInvalidAmenagement() {
    super("error_home_invalid_facilities");
  }
}