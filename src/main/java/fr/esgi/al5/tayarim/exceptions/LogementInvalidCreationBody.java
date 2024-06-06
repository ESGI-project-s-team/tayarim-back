package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsque la mise à jour d'un propriétaire contient des données invalides.
 */
public class LogementInvalidCreationBody extends RuntimeException {

  public LogementInvalidCreationBody() {
    super("error_home_invalid_creationBody");
  }
}