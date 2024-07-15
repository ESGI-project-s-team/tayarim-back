package fr.esgi.al5.tayarim.services;

import fr.esgi.al5.tayarim.dto.depense.DepenseCreationDto;
import fr.esgi.al5.tayarim.dto.depense.DepenseDto;
import fr.esgi.al5.tayarim.dto.depense.DepenseUpdateDto;
import fr.esgi.al5.tayarim.entities.Depense;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Notification;
import fr.esgi.al5.tayarim.exceptions.DepenseDateInvalidError;
import fr.esgi.al5.tayarim.exceptions.DepenseNotFoundError;
import fr.esgi.al5.tayarim.exceptions.LogementNotFoundException;
import fr.esgi.al5.tayarim.exceptions.NotificationSendError;
import fr.esgi.al5.tayarim.exceptions.UnauthorizedException;
import fr.esgi.al5.tayarim.mappers.DepenseMapper;
import fr.esgi.al5.tayarim.repositories.DepenseRepository;
import fr.esgi.al5.tayarim.repositories.LogementRepository;
import fr.esgi.al5.tayarim.repositories.NotificationRepository;
import fr.esgi.al5.tayarim.socket.MyWebSocketHandler;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe de service gérant les depenses.
 */
@Service
@Transactional(readOnly = true)
public class DepenseService {

  private final DepenseRepository depenseRepository;

  private final LogementRepository logementRepository;

  private final MyWebSocketHandler myWebSocketHandler;

  private final NotificationRepository notificationRepository;


  /**
   * Constructeur pour le service de Reservation.
   *
   * @param depenseRepository      Le repository des depenses.
   * @param logementRepository     Le repository des logements
   * @param myWebSocketHandler     Le service de socket
   * @param notificationRepository Le repository des notifications
   */
  public DepenseService(DepenseRepository depenseRepository,
      LogementRepository logementRepository,
      MyWebSocketHandler myWebSocketHandler, NotificationRepository notificationRepository) {
    this.depenseRepository = depenseRepository;
    this.logementRepository = logementRepository;
    this.myWebSocketHandler = myWebSocketHandler;
    this.notificationRepository = notificationRepository;
  }

  /**
   * Crée une nouvelle depense.
   *
   * @param depenseCreationDto Le DTO de la depense à créer.
   * @return Le DTO de la depense créée.
   */
  @Transactional
  public DepenseDto create(@NonNull DepenseCreationDto depenseCreationDto) {

    Optional<Logement> optionalLogement = logementRepository.findById(
        depenseCreationDto.getIdLogement());

    if (optionalLogement.isEmpty()) {
      throw new LogementNotFoundException();
    }

    LocalDate date;
    try {
      date = LocalDate.parse(depenseCreationDto.getDate());
    } catch (Exception e) {
      throw new DepenseDateInvalidError();
    }

    Logement logement = optionalLogement.get();

    String message = "";
    message += logement.getTitre() + ";";
    message += logement.getAdresse() + ";";
    message += logement.getVille() + ";";
    message +=
        "https://storage.googleapis.com/tayarim-tf-storage/" + logement.getImages().get(0).getUrl()
            .replace("House images", "House%20images") + ";";

    Depense depense = depenseRepository.save(
        DepenseMapper.creationDtoToEntity(
            depenseCreationDto,
            date,
            logement
        )
    );

    message += depense.getLibelle() + ";";
    message += depense.getPrix() + ";";

    try {
      myWebSocketHandler.sendNotif(logement.getProprietaire().getId(), date,
          message,
          "Depense");
    } catch (Exception e) {
      throw new NotificationSendError();
    }

    notificationRepository.save(
        new Notification(
            "Depense",
            message,
            date,
            logement.getProprietaire(),
            false
        )
    );

    return DepenseMapper.entityToDto(
        depense
    );
  }

  /**
   * Recupère toute les dépenses.
   */
  public List<DepenseDto> getAll(@NonNull Long id, @NonNull Boolean isAdmin) {

    if (isAdmin) {
      return DepenseMapper.entityListToDtoList(depenseRepository.findAll());
    }

    return DepenseMapper.entityListToDtoList(depenseRepository.findAllByLogementProprietaireId(id));

  }

  /**
   * Recupère une dépense par son id.
   */
  public DepenseDto getById(@NonNull Long id, @NonNull Long idUser, @NonNull Boolean isAdmin) {
    Optional<Depense> optionalDepense = depenseRepository.findById(id);
    if (optionalDepense.isEmpty()) {
      throw new DepenseNotFoundError();
    }

    Depense depense = optionalDepense.get();

    if (!isAdmin && depense.getLogement().getProprietaire().getId().equals(idUser)) {
      throw new UnauthorizedException();
    }

    return DepenseMapper.entityToDto(optionalDepense.get());
  }

  /**
   * Met à jour une dépense par son id.
   */
  @Transactional
  public DepenseDto update(@NonNull DepenseUpdateDto depenseUpdateDto, @NonNull Long id) {
    Optional<Depense> optionalDepense = depenseRepository.findById(id);
    if (optionalDepense.isEmpty()) {
      throw new DepenseNotFoundError();
    }

    Depense depense = optionalDepense.get();

    depense.setLibelle(
        depenseUpdateDto.getLibelle() != null && !depenseUpdateDto.getLibelle().isBlank()
            ? depenseUpdateDto.getLibelle() : depense.getLibelle()
    );

    depense.setPrix(
        depenseUpdateDto.getPrix() != null
            ? depenseUpdateDto.getPrix() : depense.getPrix()
    );

    if (depenseUpdateDto.getDate() != null && !depenseUpdateDto.getDate().isBlank()) {
      LocalDate date;
      try {
        date = LocalDate.parse(depenseUpdateDto.getDate());
        System.out.println(date);
      } catch (Exception e) {
        throw new DepenseDateInvalidError();
      }

      depense.setDate(date);
    }

    return DepenseMapper.entityToDto(depenseRepository.save(depense));

  }

  /**
   * Supprime une dépense.
   */
  @Transactional
  public DepenseDto delete(@NonNull Long id) {
    Optional<Depense> optionalDepense = depenseRepository.findById(id);
    if (optionalDepense.isEmpty()) {
      throw new DepenseNotFoundError();
    }

    Depense depense = optionalDepense.get();

    depenseRepository.deleteById(depense.getId());

    return DepenseMapper.entityToDto(depense);
  }

}
