package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.notification.NotificationDto;
import fr.esgi.al5.tayarim.entities.Notification;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Classe pour mapper les entités de Notification en DTOs Notification et vice versa.
 */
@Component
public class NotificationMapper {

  /**
   * Convertit une entité Notification en un DTO Notification.
   *
   * @param notification L'entité Notification à convertir.
   * @return Le DTO Notification correspondant.
   */
  public static NotificationDto entityToDto(@NonNull Notification notification) {
    return new NotificationDto(
        notification.getId(),
        notification.getDate().toString(),
        notification.getMessage(),
        notification.getType(),
        notification.getUtilisateur().getId(),
        notification.getIsRead()
    );
  }

  /**
   * Convertit une liste d'entités Notification en une liste de DTOs Notification.
   *
   * @param notifications La liste des Notification à convertir.
   * @return Une liste de DTOs Notification.
   */
  public static List<NotificationDto> entityListToDtoList(
      @NonNull List<Notification> notifications) {
    ArrayList<NotificationDto> notificationDtos = new ArrayList<>();
    for (Notification notification : notifications) {
      notificationDtos.add(entityToDto(notification));
    }
    return notificationDtos.stream().toList();
  }

}
