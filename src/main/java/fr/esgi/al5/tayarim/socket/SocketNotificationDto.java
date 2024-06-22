package fr.esgi.al5.tayarim.socket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Dto de notification.
 */
@Data
@AllArgsConstructor
public class SocketNotificationDto {

  @NonNull
  private String dateTime;

  @NonNull
  private String message;

  @NonNull
  private String type;
}
