package fr.esgi.al5.tayarim.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import fr.esgi.al5.tayarim.dto.notification.NotificationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationCreationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationUpdateDto;
import fr.esgi.al5.tayarim.entities.Administrateur;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Notification;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.NotificationNotFoundError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateConflictError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateInvalideError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateTooShortError;
import fr.esgi.al5.tayarim.exceptions.ReservationNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ReservationPeopleCapacityError;
import fr.esgi.al5.tayarim.exceptions.ReservationStatusUpdateError;
import fr.esgi.al5.tayarim.exceptions.ReservationStripeError;
import fr.esgi.al5.tayarim.mappers.NotificationMapper;
import fr.esgi.al5.tayarim.mappers.ReservationMapper;
import fr.esgi.al5.tayarim.repositories.AdministrateurRepository;
import fr.esgi.al5.tayarim.repositories.IndisponibiliteRepository;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.NotificationRepository;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
import fr.esgi.al5.tayarim.socket.MyWebSocketHandler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe de service gérant les notifications.
 */
@Service
@Transactional(readOnly = true)
public class NotificationService {

  /**
   * Clé secrète de l'API Stripe.
   */
  @Value("${stripe.secret-key}")
  private String stripeApiKey;

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
