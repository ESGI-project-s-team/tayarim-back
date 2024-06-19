package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.indisponibilite.IndisponibiliteCreationDto;
import fr.esgi.al5.tayarim.dto.indisponibilite.IndisponibiliteDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationCreationDto;
import fr.esgi.al5.tayarim.dto.reservation.ReservationDto;
import fr.esgi.al5.tayarim.entities.Indisponibilite;
import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Classe pour mapper les entités Indisponibilite en DTOs Indisponibilite et vice versa.
 */
@Component
public class IndisponibiliteMapper {

  /**
   * Convertit une entité Indisponibilite en un DTO Indisponibilite.
   *
   * @param dateDebut La date de début.
   * @param dateFin   La date de fin.
   * @return Le DTO Indisponibilite correspondant.
   */
  public static Indisponibilite creationDtoToEntity(
      @NonNull LocalDate dateDebut, @NonNull LocalDate dateFin, @NonNull Logement logement
  ) {

    return new Indisponibilite(
        dateDebut,
        dateFin,
        logement
    );
  }

  /**
   * Mapper entre une entity Indisponibilite et un DTO Indisponibilite.
   *
   * @param indisponibilite Indisponibilite
   */
  public static IndisponibiliteDto entityToDto(@NonNull Indisponibilite indisponibilite) {
    return new IndisponibiliteDto(
        indisponibilite.getId(),
        indisponibilite.getDateDebut().toString(),
        indisponibilite.getDateFin().toString(),
        indisponibilite.getLogement().getId()
    );
  }

  /**
   * Mapper entre une liste d'entités Indisponibilite et une liste de DTO Indisponibilite.
   *
   * @param indisponibilites Liste d'Indisponibilite
   */
  public static List<IndisponibiliteDto> entityListToDtoList(
      @NonNull List<Indisponibilite> indisponibilites) {
    List<IndisponibiliteDto> indisponibiliteDtos = new ArrayList<>();
    for (Indisponibilite indisponibilite : indisponibilites) {
      indisponibiliteDtos.add(entityToDto(indisponibilite));
    }
    return indisponibiliteDtos;
  }


}
