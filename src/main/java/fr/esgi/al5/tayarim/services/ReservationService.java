package fr.esgi.al5.tayarim.services;

import fr.esgi.al5.tayarim.dto.reservation.ReservationCreationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationUpdateDto;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ReservationNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ReservationStatusUpdateError;
import fr.esgi.al5.tayarim.mappers.ReservationMapper;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe de service gérant les reservations.
 */
@Service
@Transactional(readOnly = true)
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final LogementRepository logementRepository;


  /**
   * Constructeur pour le service de Reservation.
   *
   * @param reservationRepository Le repository des Reservation.
   * @param logementRepository    Le repository des r logements.
   */
  public ReservationService(ReservationRepository reservationRepository,
      LogementRepository logementRepository) {
    this.reservationRepository = reservationRepository;
    this.logementRepository = logementRepository;
  }

  /**
   * Tente de créer une Reservation.
   *
   * @return {@link ReservationDto}
   */
  public ReservationDto createReservation(@NonNull ReservationCreationDto reservationCreationDto) {

    LocalDate dateArrivee = LocalDate.parse(reservationCreationDto.getDateArrivee());
    LocalDate dateDepart = LocalDate.parse(reservationCreationDto.getDateDepart());

    Optional<Logement> optionalLogement = logementRepository.findById(
        reservationCreationDto.getIdLogement());
    if (optionalLogement.isEmpty()) {
      throw new LogementNotFoundException();
    }

    boolean newRandomFound = false;
    String idCommande = "";
    while (!newRandomFound) {
      idCommande = Double.toString(Math.random());
      idCommande = "RESA-".concat(idCommande.substring(idCommande.length() - 6));
      newRandomFound = reservationRepository.findByIdCommande(idCommande).isEmpty();
    }

    Logement logement = optionalLogement.get();

    if (reservationCreationDto.getMontant() == null) {
      reservationCreationDto.setMontant(
          logement.getPrixParNuit() * (dateDepart.toEpochDay() - dateArrivee.toEpochDay())
      );
    }

    return ReservationMapper.entityToDto(
        reservationRepository.save(
            ReservationMapper.creationDtoToEntity(
                reservationCreationDto,
                idCommande,
                dateArrivee,
                dateDepart,
                logement,
                LocalDateTime.now()
            )
        )
    );


  }

  /**
   * Tente de crée une Reservation.
   *
   * @return {@link ReservationDto}
   */
  @Transactional
  public ReservationDto updateReservation(@NonNull Long id,
      @NonNull ReservationUpdateDto reservationUpdateDto) {

    // tester sans les verif de tous les champs à nul, avec un body vide

    Optional<Reservation> optionalReservation = reservationRepository.findById(id);
    if (optionalReservation.isEmpty()) {
      throw new ReservationNotFoundException();
    }

    Reservation reservation = optionalReservation.get();

    reservation.setEmail(
        (reservationUpdateDto.getEmail() == null || reservationUpdateDto.getEmail().isBlank())
            ? reservation.getEmail()
            : reservationUpdateDto.getEmail()
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

    reservation.setCheckIn(
        (reservationUpdateDto.getCheckIn() == null || reservationUpdateDto.getCheckIn().isBlank())
            ? reservation.getCheckIn()
            : LocalTime.parse(reservationUpdateDto.getCheckIn())
    );

    reservation.setCheckOut(
        (reservationUpdateDto.getCheckOut() == null || reservationUpdateDto.getCheckOut().isBlank())
            ? reservation.getCheckOut()
            : LocalTime.parse(reservationUpdateDto.getCheckOut())
    );

    return ReservationMapper.entityToDto(reservationRepository.save(reservation));

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
  @Transactional
  public ReservationDto cancel(
      @NonNull Long id/*, @NonNull Boolean isAdmin, @NonNull Long idUser*/) {

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
    if (reservation.getStatut().equals("cancelled") || reservation.getStatut().equals("done")) {
      throw new ReservationStatusUpdateError();
    }

    reservation.setStatut("cancelled");

    return ReservationMapper.entityToDto(reservationRepository.save(reservation));
  }

}
