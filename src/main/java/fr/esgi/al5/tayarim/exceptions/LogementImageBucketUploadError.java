package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class LogementImageBucketUploadError extends RuntimeException {

  public LogementImageBucketUploadError() {
    super("error_logement_bucket");
  }
}