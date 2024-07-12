package fr.esgi.al5.tayarim.services;

import fr.esgi.al5.tayarim.dto.notification.NotificationDto;
import fr.esgi.al5.tayarim.entities.Notification;
import fr.esgi.al5.tayarim.exceptions.NotificationNotFoundError;
import fr.esgi.al5.tayarim.mappers.NotificationMapper;
import fr.esgi.al5.tayarim.repositories.NotificationRepository;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe de service gérant les notifications.
 */
@Service
@Transactional(readOnly = true)
public class NotificationService {

  private final NotificationRepository notificationRepository;


  /**
   * Constructeur pour le service de Reservation.
   *
   * @param notificationRepository Le repository des notifications.
   */
  public NotificationService(NotificationRepository notificationRepository) {

    this.notificationRepository = notificationRepository;
  }

  /**
   * Récupère toutes les notifications.
   *
   * @return Liste des notifications.
   */
  public List<NotificationDto> getAll(@NonNull Long id) {

    return NotificationMapper.entityListToDtoList(
        notificationRepository.findAllByUtilisateurId(id));

  }

  /**
   * Marque une notification comme lue.
   */
  @Transactional
  public NotificationDto read(@NonNull Long id) {

    Optional<Notification> optionalNotification = notificationRepository.findById(id);

    if (optionalNotification.isEmpty()) {
      throw new NotificationNotFoundError();
    }

    Notification notification = optionalNotification.get();
    notification.setIsRead(true);

    return NotificationMapper.entityToDto(notificationRepository.save(notification));

  }


}
