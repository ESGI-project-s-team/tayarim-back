package fr.esgi.al5.tayarim.exceptions;

/**
 * Exception lancée lorsqu'un utilisateur spécifié n'est pas trouvé dans le système.
 */
public class NotificationSendError extends RuntimeException {

  public NotificationSendError() {
    super("error_notification_send");
  }
}