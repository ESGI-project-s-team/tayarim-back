package fr.esgi.al5.tayarim.services;

import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.dto.regleslogement.ReglesLogementDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationCreationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationUpdateDto;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ReservationNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ReservationStatusUpdateError;
import fr.esgi.al5.tayarim.mappers.ReglesLogementMapper;
import fr.esgi.al5.tayarim.mappers.ReservationMapper;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.ReglesLogementRepository;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
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
   * Tente de crée une Reservation.
   *
   * @return {@link ReservationDto}
   */
  public ReservationDto createReservation(@NonNull ReservationCreationDto reservationCreationDto) {

    LocalTime checkIn = LocalTime.parse(reservationCreationDto.getCheckIn());
    LocalTime checkOut = LocalTime.parse(reservationCreationDto.getCheckOut());

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

    return ReservationMapper.entityToDto(
        reservationRepository.save(
            ReservationMapper.creationDtoToEntity(
                reservationCreationDto,
                idCommande,
                checkIn,
                checkOut,
                optionalLogement.get()
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
  public List<ReservationDto> getAllReservation(@NonNull Boolean isAdmin,
      @NonNull Long idProprietaire) {

    if (isAdmin) {
      return ReservationMapper.entityListToDtoList(reservationRepository.findAll());
    }

    return ReservationMapper.entityListToDtoList(
        reservationRepository.findAllByIdProprietaire(idProprietaire));

  }

  /**
   * Tente de récupèrer une reservation par son id.
   *
   * @return {@link ReservationDto}
   */
  public ReservationDto getReservationById(@NonNull Long id) {

    Optional<Reservation> optionalReservation = reservationRepository.findById(id);
    if (optionalReservation.isEmpty()) {
      throw new ReservationNotFoundException();
    }

    return ReservationMapper.entityToDto(optionalReservation.get());

  }

  /**
   * Valide l'etape del a reservation et passe à la suivante.
   *
   * @return {@link ReservationDto}
   */
  @Transactional
  public ReservationDto nextStep(@NonNull Long id) {

    Optional<Reservation> optionalReservation = reservationRepository.findById(id);
    if (optionalReservation.isEmpty()) {
      throw new ReservationNotFoundException();
    }

    Reservation reservation = optionalReservation.get();
    switch (reservation.getStatut()) {
      case "waiting" -> reservation.setStatut("validate");
      case "validate" -> reservation.setStatut("refused");
      case "refused" -> reservation.setStatut("payed");
      case "payed" -> reservation.setStatut("done");
      default -> throw new ReservationStatusUpdateError();
    }

    return ReservationMapper.entityToDto(reservationRepository.save(reservation));

  }

}
