package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsque la mise à jour d'un propriétaire contient des données invalides.
 */
public class LogementInvalidUpdateBody extends RuntimeException {

  public LogementInvalidUpdateBody() {
    super("error_owner_invalid_updateBody");
  }
}