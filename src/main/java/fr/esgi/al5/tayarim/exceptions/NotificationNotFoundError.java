package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class NotificationNotFoundError extends RuntimeException {

  public NotificationNotFoundError() {
    super("error_notification_notExist");
  }
}