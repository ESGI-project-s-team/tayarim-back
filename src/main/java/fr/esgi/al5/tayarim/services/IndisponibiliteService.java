package fr.esgi.al5.tayarim.services;

import fr.esgi.al5.tayarim.dto.indisponibilite.IndisponibiliteCreationDto;
import fr.esgi.al5.tayarim.dto.indisponibilite.IndisponibiliteDto;
import fr.esgi.al5.tayarim.entities.Administrateur;
import fr.esgi.al5.tayarim.entities.Indisponibilite;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Notification;
import fr.esgi.al5.tayarim.exceptions.IndisponibiliteDateInvalidError;
import fr.esgi.al5.tayarim.exceptions.IndisponibiliteLogementNotFoundError;
import fr.esgi.al5.tayarim.exceptions.IndisponibiliteNotFoundError;
import fr.esgi.al5.tayarim.exceptions.IndisponibiliteUnauthorizedError;
import fr.esgi.al5.tayarim.exceptions.NotificationSendError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateConflictError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateInvalideError;
import fr.esgi.al5.tayarim.mappers.IndisponibiliteMapper;
import fr.esgi.al5.tayarim.repositories.AdministrateurRepository;
import fr.esgi.al5.tayarim.repositories.IndisponibiliteRepository;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.NotificationRepository;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
import fr.esgi.al5.tayarim.socket.MyWebSocketHandler;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe de service gérant les Indisponibilite.
 */
@Service
@Transactional(readOnly = true)
public class IndisponibiliteService {

  private final IndisponibiliteRepository indisponibiliteRepository;

  private final LogementRepository logementRepository;

  private final ReservationRepository reservationRepository;

  private final MyWebSocketHandler myWebSocketHandler;

  private final AdministrateurRepository administrateurRepository;

  private final NotificationRepository notificationRepository;


  /**
   * Constructeur pour le service de Indisponibilite.
   *
   * @param indisponibiliteRepository Le repository des Indisponibilite.
   * @param logementRepository        Le repository des Logements.
   * @param reservationRepository     Le repository des Reservation.
   * @param myWebSocketHandler        Le service de socket.
   * @param administrateurRepository  Le service d'administrateur
   * @param notificationRepository    Le repository des notifications
   */
  public IndisponibiliteService(IndisponibiliteRepository indisponibiliteRepository,
      LogementRepository logementRepository, ReservationRepository reservationRepository,
      MyWebSocketHandler myWebSocketHandler, AdministrateurRepository administrateurRepository,
      NotificationRepository notificationRepository) {
    this.indisponibiliteRepository = indisponibiliteRepository;
    this.logementRepository = logementRepository;
    this.reservationRepository = reservationRepository;
    this.myWebSocketHandler = myWebSocketHandler;
    this.administrateurRepository = administrateurRepository;
    this.notificationRepository = notificationRepository;
  }

  /**
   * Crée une nouvelle Indisponibilite.
   *
   * @param indisponibiliteCreationDto Dto de création de Indisponibilite.
   */
  @Transactional
  public IndisponibiliteDto create(@NonNull IndisponibiliteCreationDto indisponibiliteCreationDto,
      @NonNull Long idUser, @NonNull Boolean isAdmin) {

    Optional<Logement> optionalLogement = logementRepository.findById(
        indisponibiliteCreationDto.getIdLogement());

    if (optionalLogement.isEmpty()) {
      throw new IndisponibiliteLogementNotFoundError();
    }

    if (!isAdmin && !optionalLogement.get().getProprietaire().getId().equals(idUser)) {
      throw new IndisponibiliteUnauthorizedError();
    }
    LocalDate dateDebut;
    LocalDate dateFin;
    try {
      dateDebut = LocalDate.parse(indisponibiliteCreationDto.getDateDebut());
      dateFin = LocalDate.parse(indisponibiliteCreationDto.getDateFin());
    } catch (Exception e) {
      throw new IndisponibiliteDateInvalidError();
    }

    if (dateDebut.isAfter(dateFin)) {
      throw new IndisponibiliteDateInvalidError();
    }

    Logement logement = optionalLogement.get();

    checkDateCondition(dateDebut, dateFin, isAdmin);

    checkDateConclict(
        "INDISPONIBILITE-".concat(logement.getId().toString()),
        dateDebut,
        dateFin,
        logement.getId(), isAdmin);

    Indisponibilite indisponibilite = indisponibiliteRepository.save(
        IndisponibiliteMapper.creationDtoToEntity(
            dateDebut,
            dateFin,
            logement
        )
    );

    System.out.println("sendNotif : " + logement.getTitre() + " " + isAdmin);
    sendNotif(logement, isAdmin, indisponibilite);

    return IndisponibiliteMapper.entityToDto(
        indisponibilite
    );
  }

  private void sendNotif(@NonNull Logement logement, @NonNull Boolean isAdmin,
      @NonNull Indisponibilite indisponibilite) {
    String message = "";
    message += logement.getTitre() + ";";
    message += logement.getAdresse() + ";";
    message += logement.getVille() + ";";
    message +=
        "https://storage.googleapis.com/tayarim-tf-storage/" + logement.getImages().get(0).getUrl()
            .replace("House images", "House%20images") + ";";
    message += indisponibilite.getDateDebut() + ";";
    message += indisponibilite.getDateFin() + ";";

    if (isAdmin) {
      try {
        myWebSocketHandler.sendNotif(logement.getProprietaire().getId(), LocalDate.now(),
            message, "Indisponibilite");
      } catch (Exception ignored) {
        // Ignored
      }
      notificationRepository.save(new Notification(
          "Indisponibilite",
          message,
          LocalDate.now(),
          logement.getProprietaire(),
          false
      ));
    } else {
      System.out.println("not admin");
      for (Administrateur administrateur : administrateurRepository.findAll()) {
        try {
          myWebSocketHandler.sendNotif(administrateur.getId(), LocalDate.now(),
              message, "Indisponibilite");
        } catch (Exception ignored) {
          // Ignored
        }

        notificationRepository.save(new Notification(
            "Indisponibilite",
            message,
            LocalDate.now(),
            administrateur,
            false
        ));
      }
    }
  }

  /**
   * Récupère toutes les Indisponibilite.
   *
   * @return Liste de Indisponibilite.
   */
  public List<IndisponibiliteDto> getAll() {
    return IndisponibiliteMapper.entityListToDtoList(indisponibiliteRepository.findAll());
  }

  /**
   * Récupère une Indisponibilite par son id.
   *
   * @param id L'id de la Indisponibilite.
   * @return La Indisponibilite.
   */
  public IndisponibiliteDto getById(@NonNull Long id) {
    return IndisponibiliteMapper.entityToDto(
        indisponibiliteRepository.findById(id).orElseThrow()
    );
  }

  /**
   * Supprime une Indisponibilite par son id.
   *
   * @param id L'id de la Indisponibilite.
   * @return La Indisponibilite.
   */
  @Transactional
  public IndisponibiliteDto delete(@NonNull Long id) {

    Optional<Indisponibilite> optionalIndisponibilite = indisponibiliteRepository.findById(id);
    if (optionalIndisponibilite.isEmpty()) {
      throw new IndisponibiliteNotFoundError();
    }

    IndisponibiliteDto indisponibiliteDto = IndisponibiliteMapper.entityToDto(
        optionalIndisponibilite.get());

    indisponibiliteRepository.deleteById(id);
    return indisponibiliteDto;
  }

  /**
   * Vérifie les conditions d'application des dates.
   */
  public void checkDateCondition(@NonNull LocalDate dateArrivee,
      @NonNull LocalDate dateDepart, @NonNull Boolean isAdmin) {
    if (dateArrivee.toEpochDay() >= dateDepart.toEpochDay()) {
      throw new ReservationDateInvalideError();
    }

    if (!isAdmin && dateArrivee.toEpochDay() <= LocalDate.now().toEpochDay() + 2) {
      throw new ReservationDateInvalideError();
    }

  }

  /**
   * Vérifie les conflits de dates.
   */
  public void checkDateConclict(@NonNull String actualResa, @NonNull LocalDate dateArrivee,
      @NonNull LocalDate dateDepart, @NonNull Long idLogement, @NonNull Boolean isAdmin) {

    Map<String, List<LocalDate>> dates = new java.util.HashMap<>(Map.of());

    reservationRepository.findAllByLogementIdAndStatutIn(
        idLogement, List.of("reserved", "payed", "in progress")
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

      if (arrivee.toEpochDay() > dateDepart.toEpochDay()) {
        continue;
      }
      if (depart.toEpochDay() < dateArrivee.toEpochDay()) {
        continue;
      }

      throw new ReservationDateConflictError();

    }
  }
}
