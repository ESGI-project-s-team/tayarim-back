package fr.esgi.al5.tayarim.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import fr.esgi.al5.tayarim.dto.reservation.ReservationCreationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationFindDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationUpdateDto;
import fr.esgi.al5.tayarim.entities.Administrateur;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Notification;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.NotificationSendError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateConflictError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateInvalideError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateTooShortError;
import fr.esgi.al5.tayarim.exceptions.ReservationNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ReservationPeopleCapacityError;
import fr.esgi.al5.tayarim.exceptions.ReservationStatusUpdateError;
import fr.esgi.al5.tayarim.exceptions.ReservationStripeError;
import fr.esgi.al5.tayarim.mail.EmailService;
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
 * Classe de service gérant les reservations.
 */
@Service
@Transactional(readOnly = true)
public class ReservationService {

  /**
   * Clé secrète de l'API Stripe.
   */
  @Value("${stripe.secret-key}")
  private String stripeApiKey;

  private final ReservationRepository reservationRepository;
  private final LogementRepository logementRepository;

  private final IndisponibiliteRepository indisponibiliteRepository;
  private final MyWebSocketHandler myWebSocketHandler;

  private final AdministrateurRepository administrateurRepository;

  private final NotificationRepository notificationRepository;

  private final EmailService emailService;


  /**
   * Constructeur pour le service de Reservation.
   *
   * @param reservationRepository     Le repository des Reservation.
   * @param logementRepository        Le repository des r logements.
   * @param indisponibiliteRepository Le repository des indisponibilites.
   * @param myWebSocketHandler        Le service de socket.
   * @param administrateurRepository  Le service d'administrateur
   * @param notificationRepository    Le repository des notifications
   * @param emailService              Le service d'envoi de mail
   */
  public ReservationService(ReservationRepository reservationRepository,
      LogementRepository logementRepository, IndisponibiliteRepository indisponibiliteRepository,
      MyWebSocketHandler myWebSocketHandler, AdministrateurRepository administrateurRepository,
      NotificationRepository notificationRepository, EmailService emailService) {
    this.reservationRepository = reservationRepository;
    this.logementRepository = logementRepository;
    this.indisponibiliteRepository = indisponibiliteRepository;
    this.myWebSocketHandler = myWebSocketHandler;
    this.administrateurRepository = administrateurRepository;
    this.notificationRepository = notificationRepository;
    this.emailService = emailService;
  }

  /**
   * Tente de créer une Reservation.
   *
   * @return {@link ReservationDto}
   */
  @Transactional
  public ReservationDto createReservation(@NonNull ReservationCreationDto reservationCreationDto,
      @NonNull Boolean isAdmin) {

    Optional<Logement> optionalLogement = logementRepository.findById(
        reservationCreationDto.getIdLogement());
    if (optionalLogement.isEmpty()) {
      throw new LogementNotFoundException();
    }

    if (!isAdmin && reservationCreationDto.getNbPersonnes() > optionalLogement.get()
        .getCapaciteMaxPersonne()) {
      throw new ReservationPeopleCapacityError();
    }

    boolean newRandomFound = false;
    String idCommande = "";
    while (!newRandomFound) {
      idCommande = Double.toString(Math.random());
      idCommande = "RESA-".concat(idCommande.substring(idCommande.length() - 6));
      newRandomFound = reservationRepository.findByIdCommande(idCommande).isEmpty();
    }

    LocalDate dateArrivee = LocalDate.parse(reservationCreationDto.getDateArrivee());
    LocalDate dateDepart = LocalDate.parse(reservationCreationDto.getDateDepart());

    checkDateCondition(dateArrivee, dateDepart, optionalLogement.get().getNombresNuitsMin(),
        isAdmin);

    checkDateConclict(idCommande, dateArrivee, dateDepart, reservationCreationDto.getIdLogement(),
        isAdmin);

    Logement logement = optionalLogement.get();

    if (reservationCreationDto.getMontant() == null) {
      reservationCreationDto.setMontant(
          logement.getPrixParNuit() * (dateDepart.toEpochDay() - dateArrivee.toEpochDay())
      );
    }

    String status;
    if (isAdmin) {
      status = "payed";
    } else {
      status = "reserved";
    }

    try {
      sendNotif(logement, isAdmin);
    } catch (Exception e) {
      throw new NotificationSendError();
    }

    Reservation reservation = reservationRepository.save(
        ReservationMapper.creationDtoToEntity(
            reservationCreationDto,
            idCommande,
            status,
            dateArrivee,
            dateDepart,
            logement,
            LocalDateTime.now(),
            reservationCreationDto.getPaymentIntent()
        )
    );

    System.out.println(reservation.getLogement().getImages().get(0).getUrl().replace(" ", "%20"));

    emailService.sendCreationReservationEmail(
        reservation.getEmail(),
        reservation.getNom(),
        reservation.getPrenom(),
        reservation.getIdCommande(),
        LocalDate.now().toString(),
        reservation.getMontant().toString(),
        "https://storage.googleapis.com/tayarim-tf-storage/"
            + reservation.getLogement().getImages().get(0).getUrl().replace(" ", "%20"),
        reservation.getLogement().getAdresse(),
        reservation.getDateArrivee().toString(),
        Long.toString(
            reservation.getDateDepart().toEpochDay() - reservation.getDateArrivee().toEpochDay()),
        reservation.getNbPersonnes().toString()

    );

    return ReservationMapper.entityToDto(
        reservation
    );


  }

  private void sendNotif(@NonNull Logement logement, @NonNull Boolean isAdmin) {

    myWebSocketHandler.sendNotif(logement.getProprietaire().getId(), LocalDate.now(),
        "notification_reservation_creation", "Reservation");

    notificationRepository.save(new Notification(
        "Reservation",
        "notification_reservation_creation",
        LocalDate.now(),
        logement.getProprietaire(),
        false
    ));

    if (!isAdmin) {
      //ici
      for (Administrateur administrateur : administrateurRepository.findAll()) {
        myWebSocketHandler.sendNotif(administrateur.getId(), LocalDate.now(),
            "notification_reservation_creation", "Reservation");

        notificationRepository.save(new Notification(
            "Reservation",
            "notification_reservation_creation",
            LocalDate.now(),
            administrateur,
            false
        ));
      }
    }


  }

  /**
   * Tente de mettre à jour le paymentIntent.
   *
   * @param id            id de la reservation
   * @param paymentIntent paymentIntent de la réservation
   */
  @Transactional
  public ReservationDto updateReservationPaymentIntent(@NonNull Long id,
      @NonNull String paymentIntent) {
    Optional<Reservation> optionalReservation = reservationRepository.findById(id);
    if (optionalReservation.isEmpty()) {
      throw new ReservationNotFoundException();
    } else {
      Reservation reservation = optionalReservation.get();
      reservation.setPaymentIntent(paymentIntent);
      return ReservationMapper.entityToDto(reservationRepository.save(reservation));
    }
  }


  /**
   * Tente de crée une Reservation.
   *
   * @return {@link ReservationDto}
   */
  @Transactional
  public ReservationDto updateReservation(@NonNull Long id,
      @NonNull ReservationUpdateDto reservationUpdateDto, @NonNull Boolean isAdmin) {

    // tester sans les verif de tous les champs à nul, avec un body vide

    Optional<Reservation> optionalReservation = reservationRepository.findById(id);
    if (optionalReservation.isEmpty()) {
      throw new ReservationNotFoundException();
    }

    Reservation reservation = optionalReservation.get();

    if (!isAdmin
        && reservationUpdateDto.getNbPersonnes() != null
        && reservationUpdateDto.getNbPersonnes() > reservation.getLogement()
        .getCapaciteMaxPersonne()) {
      throw new ReservationPeopleCapacityError();
    }

    reservation.setEmail(
        (reservationUpdateDto.getEmail() == null || reservationUpdateDto.getEmail().isBlank())
            ? reservation.getEmail()
            : reservationUpdateDto.getEmail()
    );

    reservation.setNumTel(
        (reservationUpdateDto.getNumTel() == null || reservationUpdateDto.getNumTel().isBlank())
            ? reservation.getNumTel()
            : reservationUpdateDto.getNumTel()
    );

    reservation.setNom(
        (reservationUpdateDto.getNom() == null || reservationUpdateDto.getNom().isBlank())
            ? reservation.getNom()
            : reservationUpdateDto.getNom()
    );

    reservation.setPrenom(
        (reservationUpdateDto.getPrenom() == null || reservationUpdateDto.getPrenom().isBlank())
            ? reservation.getPrenom()
            : reservationUpdateDto.getPrenom()
    );

    reservation.setNbPersonnes(
        (reservationUpdateDto.getNbPersonnes() == null)
            ? reservation.getNbPersonnes()
            : reservationUpdateDto.getNbPersonnes()
    );

    reservation.setMontant(
        (reservationUpdateDto.getMontant() == null)
            ? reservation.getMontant()
            : reservationUpdateDto.getMontant()
    );

    reservation.setDateArrivee(
        (reservationUpdateDto.getDateArrivee() == null || reservationUpdateDto.getDateArrivee()
            .isBlank())
            ? reservation.getDateArrivee()
            : LocalDate.parse(reservationUpdateDto.getDateArrivee())
    );

    reservation.setDateDepart(
        (reservationUpdateDto.getDateDepart() == null || reservationUpdateDto.getDateDepart()
            .isBlank())
            ? reservation.getDateDepart()
            : LocalDate.parse(reservationUpdateDto.getDateDepart())
    );

    checkDateCondition(reservation.getDateArrivee(), reservation.getDateDepart(),
        reservation.getLogement().getNombresNuitsMin(), isAdmin);

    checkDateConclict(reservation.getIdCommande(), reservation.getDateArrivee(),
        reservation.getDateDepart(), reservation.getLogement().getId(), isAdmin);

    reservation = reservationRepository.save(reservation);

    System.out.println();
    emailService.sendModificationReservationEmail(
        reservation.getEmail(),
        reservation.getNom(),
        reservation.getPrenom(),
        reservation.getIdCommande(),
        reservation.getDateReservation().toLocalDate().toString(),
        reservation.getMontant().toString(),
        "https://storage.googleapis.com/tayarim-tf-storage/"
            + reservation.getLogement().getImages().get(0).getUrl(),
        reservation.getLogement().getAdresse(),
        reservation.getDateArrivee().toString(),
        Long.toString(
            reservation.getDateDepart().toEpochDay() - reservation.getDateArrivee().toEpochDay()),
        reservation.getNbPersonnes().toString()
    );

    return ReservationMapper.entityToDto(reservation);

  }

  /**
   * Tente de récupèrer les reservations.
   *
   * @return {@link ReservationDto}
   */
  @Transactional
  public List<ReservationDto> getAllReservation(@NonNull Boolean isAdmin,
      @NonNull Long idProprietaire) {

    if (isAdmin) {
      return ReservationMapper.entityListToDtoList(
          reservationRepository.findAll().stream().map(this::checkStatus).toList());
    }

    return ReservationMapper.entityListToDtoList(
        reservationRepository.findAllByIdProprietaire(idProprietaire).stream()
            .map(this::checkStatus).toList());
  }

  /**
   * Tente de récupérer une reservation par son id.
   *
   * @return {@link ReservationDto}
   */
  @Transactional
  public ReservationDto getReservationById(@NonNull Long id) {

    Optional<Reservation> optionalReservation = reservationRepository.findById(id);
    if (optionalReservation.isEmpty()) {
      throw new ReservationNotFoundException();
    }

    return ReservationMapper.entityToDto(checkStatus(optionalReservation.get()));

  }

  /**
   * Regarde le status et le change si besoin.
   *
   * @return {@link ReservationDto}
   */
  @Transactional
  public Reservation checkStatus(@NonNull Reservation reservation) {

    if (reservation.getStatut().equals("done")) {
      return reservation;
    }

    LocalDateTime now = LocalDateTime.now();
    if ((reservation.getStatut().equals("payed") || reservation.getStatut().equals("in progress"))
        && ((now.toLocalDate().toEpochDay() == reservation.getDateDepart().toEpochDay()
        && now.toLocalTime().isAfter(reservation.getCheckOut())) || (now.toLocalDate().toEpochDay()
        > reservation.getDateDepart().toEpochDay()))) {
      reservation.setStatut("done");
      return reservationRepository.save(reservation);
    }

    if (reservation.getStatut().equals("payed")
        && (now.toLocalDate().toEpochDay() == reservation.getDateArrivee().toEpochDay()
        && now.toLocalTime().isAfter(reservation.getCheckIn()) || (
        now.toLocalDate().toEpochDay() > reservation.getDateArrivee().toEpochDay()))
    ) {
      reservation.setStatut("in progress");
      return reservationRepository.save(reservation);
    }

    return reservation;

  }

  /**
   * Tente d'annuler une reservation.
   *
   * @return {@link ReservationDto}
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public ReservationDto cancel(
      @NonNull Long id) {

    Optional<Reservation> optionalReservation = reservationRepository.findById(id);
    if (optionalReservation.isEmpty()) {
      throw new ReservationNotFoundException();
    }

    /*
    if(!isAdmin ){
      throw new ReservationStatusUpdateError();
    }
    */

    Reservation reservation = optionalReservation.get();
    if (!reservation.getStatut().equals("reserved") && !reservation.getStatut().equals("payed")) {
      throw new ReservationStatusUpdateError();
    }

    Stripe.apiKey = stripeApiKey;
    try {
      PaymentIntent paymentIntent = PaymentIntent.retrieve(
          reservation.getPaymentIntent());
      paymentIntent.cancel();
    } catch (StripeException e) {
      throw new ReservationStripeError();
    }

    reservation.setStatut("cancelled");
    reservation = reservationRepository.save(reservation);

    emailService.sendAnnulationReservationEmail(
        reservation.getEmail(),
        reservation.getNom(),
        reservation.getPrenom(),
        reservation.getIdCommande(),
        reservation.getDateReservation().toLocalDate().toString(),
        reservation.getMontant().toString(),
        "https://storage.googleapis.com/tayarim-tf-storage/"
            + reservation.getLogement().getImages().get(0).getUrl(),
        reservation.getLogement().getAdresse(),
        reservation.getDateArrivee().toString(),
        Long.toString(
            reservation.getDateDepart().toEpochDay() - reservation.getDateArrivee().toEpochDay()),
        reservation.getNbPersonnes().toString()

    );

    return ReservationMapper.entityToDto(reservation);
  }

  /**
   * Vérifie les conditions d'application des dates.
   */
  public void checkDateCondition(@NonNull LocalDate dateArrivee,
      @NonNull LocalDate dateDepart, @NonNull Integer nombresNuitsMin, @NonNull Boolean isAdmin) {
    if (dateArrivee.toEpochDay() >= dateDepart.toEpochDay()) {
      throw new ReservationDateInvalideError();
    }

    if (!isAdmin && dateArrivee.toEpochDay() <= LocalDate.now().toEpochDay() + 2) {
      throw new ReservationDateInvalideError();
    }

    if (!isAdmin && dateDepart.toEpochDay() - dateArrivee.toEpochDay() < nombresNuitsMin) {
      throw new ReservationDateTooShortError();
    }
  }

  /**
   * Vérifie les conflits de dates.
   */
  public void checkDateConclict(@NonNull String actualResa, @NonNull LocalDate dateArrivee,
      @NonNull LocalDate dateDepart, @NonNull Long idLogement, @NonNull Boolean isAdmin) {

    Map<String, List<LocalDate>> dates = new java.util.HashMap<>(Map.of());

    reservationRepository.findAllByLogementIdAndStatutIn(
        idLogement, List.of("reservation", "payed", "in progress")
    ).forEach(reservation -> dates.put(reservation.getIdCommande(),
        List.of(reservation.getDateArrivee(), reservation.getDateDepart())));

    indisponibiliteRepository.findAllByLogementId(idLogement).forEach(indisponibilite -> dates
        .put("INDISPONIBILITE-".concat(indisponibilite.getId().toString()),
            List.of(indisponibilite.getDateDebut(), indisponibilite.getDateFin())));

    // TODO ajouter un filtre sur les dates pour ne pas prendre ls indisponibilités trop anciennes

    for (Entry<String, List<LocalDate>> entry : dates.entrySet()) {
      if (entry.getKey().equals(actualResa)) {
        continue;
      }

      LocalDate arrivee = entry.getValue().get(0);
      LocalDate depart = entry.getValue().get(1);

      if (!isAdmin) {
        if (arrivee.toEpochDay() > (dateDepart.toEpochDay() + 2)) {
          continue;
        }
        if (depart.toEpochDay() < (dateArrivee.toEpochDay())) {
          continue;
        }
      } else {
        if (arrivee.toEpochDay() > dateDepart.toEpochDay()) {
          continue;
        }
        if (depart.toEpochDay() < dateArrivee.toEpochDay()) {
          continue;
        }
      }

      throw new ReservationDateConflictError();

    }
  }

  /**
   * Valide une réservation.
   *
   * @param id Id de la réservation.
   */
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public ReservationDto validate(@NonNull Long id) {
    Optional<Reservation> optionalReservation = reservationRepository.findById(id);

    if (optionalReservation.isEmpty()) {
      throw new ReservationNotFoundException();
    }

    Reservation reservation = optionalReservation.get();

    try {
      PaymentIntent paymentIntent = PaymentIntent.retrieve(reservation.getPaymentIntent());
      paymentIntent.capture();
    } catch (StripeException e) {
      throw new ReservationStripeError();
    }

    reservation.setStatut("payed");

    return ReservationMapper.entityToDto(reservationRepository.save(reservation));
  }

  /**
   * Trouve la réservation d'un client.
   */
  public ReservationDto find(@NonNull ReservationFindDto reservationFindDto) {

    Optional<Reservation> optionalReservation = reservationRepository
        .findClientReservation(
            reservationFindDto.getCode(),
            reservationFindDto.getIdentifier(),
            LocalDate.parse(reservationFindDto.getDateArrivee())
        );

    if (optionalReservation.isEmpty()) {
      throw new ReservationNotFoundException();
    }

    return ReservationMapper.entityToDto(optionalReservation.get());

  }

  /**
   * Envoie un message.
   */
  public void message(@NonNull Long idCommande, @NonNull String message) {

    Optional<Reservation> optionalReservation = reservationRepository.findById(idCommande);

    if (optionalReservation.isEmpty()) {
      throw new ReservationNotFoundException();
    }

    Reservation reservation = optionalReservation.get();

    administrateurRepository.findAll().forEach(
        administrateur -> emailService.sendClientMessageEmail(message, administrateur.getEmail(),
            reservation.getEmail(), reservation.getIdCommande(), reservation.getNom(),
            reservation.getPrenom()));

  }
}
