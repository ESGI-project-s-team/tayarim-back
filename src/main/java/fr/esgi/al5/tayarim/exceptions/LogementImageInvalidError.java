package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class LogementImageInvalidError extends RuntimeException {

  public LogementImageInvalidError() {
    super("error_logement_image_invalid");
  }
}