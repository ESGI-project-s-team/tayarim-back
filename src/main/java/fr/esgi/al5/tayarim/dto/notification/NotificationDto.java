package fr.esgi.al5.tayarim.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * DTO for Notification.
 */
@Data // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor // Generates an all-args constructor
public class NotificationDto {

  @NonNull
  private Long id;

  @NonNull
  private String date;

  @NonNull
  private String message;

  @NonNull
  private String type;

  @NonNull
  private Long idUser;

  @NonNull
  private Boolean isRead;


}
