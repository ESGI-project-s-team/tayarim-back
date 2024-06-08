package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un logement spécifié n'est pas trouvé dans le système.
 */
public class LogementNotFoundException extends RuntimeException {

  public LogementNotFoundException() {
    super("error_home_notExist");
  }
}