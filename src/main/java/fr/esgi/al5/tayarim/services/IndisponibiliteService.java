package fr.esgi.al5.tayarim.services;

import fr.esgi.al5.tayarim.dto.indisponibilite.IndisponibiliteCreationDto;
import fr.esgi.al5.tayarim.dto.indisponibilite.IndisponibiliteDto;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.exceptions.IndisponibiliteDateInvalidError;
import fr.esgi.al5.tayarim.exceptions.IndisponibiliteLogementNotFoundError;
import fr.esgi.al5.tayarim.exceptions.IndisponibiliteUnauthorizedError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateConflictError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateInvalideError;
import fr.esgi.al5.tayarim.exceptions.ReservationDateTooShortError;
import fr.esgi.al5.tayarim.mappers.IndisponibiliteMapper;
import fr.esgi.al5.tayarim.repositories.IndisponibiliteRepository;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
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


  /**
   * Constructeur pour le service de Indisponibilite.
   *
   * @param indisponibiliteRepository Le repository des Indisponibilite.
   * @param logementRepository        Le repository des Logements.
   * @param reservationRepository     Le repository des Reservation.
   */
  public IndisponibiliteService(IndisponibiliteRepository indisponibiliteRepository,
      LogementRepository logementRepository, ReservationRepository reservationRepository) {
    this.indisponibiliteRepository = indisponibiliteRepository;
    this.logementRepository = logementRepository;
    this.reservationRepository = reservationRepository;
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

    LocalDate dateDebut = LocalDate.parse(indisponibiliteCreationDto.getDateDebut());
    LocalDate dateFin = LocalDate.parse(indisponibiliteCreationDto.getDateFin());

    if (dateDebut.isAfter(dateFin)) {
      throw new IndisponibiliteDateInvalidError();
    }

    Logement logement = optionalLogement.get();

    checkDateCondition(dateDebut, dateFin, logement.getNombresNuitsMin(), isAdmin);

    checkDateConclict(
        "INDISPONIBILITE-".concat(logement.getId().toString()),
        dateDebut,
        dateFin,
        logement.getId(), isAdmin);

    return IndisponibiliteMapper.entityToDto(
        indisponibiliteRepository.save(
            IndisponibiliteMapper.creationDtoToEntity(
                dateDebut,
                dateFin,
                logement
            )
        )
    );
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
        idLogement, List.of("payed", "in progress")
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
        if (depart.toEpochDay() < (dateArrivee.toEpochDay() - 2)) {
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
}
