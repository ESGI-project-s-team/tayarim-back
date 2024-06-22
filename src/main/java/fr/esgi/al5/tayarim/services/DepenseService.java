package fr.esgi.al5.tayarim.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import fr.esgi.al5.tayarim.dto.depense.DepenseCreationDto;
import fr.esgi.al5.tayarim.dto.depense.DepenseDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationCreationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationUpdateDto;
import fr.esgi.al5.tayarim.entities.Depense;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ReservationDateConflictError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateInvalideError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateTooShortError;
import fr.esgi.al5.tayarim.exceptions.ReservationNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ReservationPeopleCapacityError;
import fr.esgi.al5.tayarim.exceptions.ReservationStatusUpdateError;
import fr.esgi.al5.tayarim.exceptions.ReservationStripeError;
import fr.esgi.al5.tayarim.mappers.ReservationMapper;
import fr.esgi.al5.tayarim.repositories.DepenseRepository;
import fr.esgi.al5.tayarim.repositories.IndisponibiliteRepository;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
import fr.esgi.al5.tayarim.socket.MyWebSocketHandler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe de service gérant les depenses.
 */
@Service
@Transactional(readOnly = true)
public class DepenseService {

  private final DepenseRepository depenseRepository;

  private final AuthService authService;



  /**
   * Constructeur pour le service de Reservation.
   *
   * @param depenseRepository Le repository des depenses.
   * @param authService     Le service d'authentification.
   */
  public DepenseService(DepenseRepository depenseRepository,
      AuthService authService) {
    this.depenseRepository = depenseRepository;
    this.authService = authService;
  }

  /**
   * Crée une nouvelle depense.
   *
   * @param depenseCreationDto Le DTO de la depense à créer.
   * @return Le DTO de la depense créée.
   */
  public DepenseDto create(@NonNull DepenseCreationDto depenseCreationDto) {
    /*return DepenseMapper.entityToDto(
        depenseRepository.save(
            DepenseMapper.creationDtoToEntity(
                depenseCreationDto
            )
        )
    );*/
    return null;
  }

}
