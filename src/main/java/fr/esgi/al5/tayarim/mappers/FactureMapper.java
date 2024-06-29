package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.facture.FactureCreationDto;
import fr.esgi.al5.tayarim.dto.facture.FactureDto;
import fr.esgi.al5.tayarim.entities.Facture;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * Classe pour mapper les entités de Type de Logement en DTOs TypeDeLogement et vice versa.
 */
@Component
public class FactureMapper {

  /**
   * Convertit un DTO de création de Depense en une entité Depense.
   *
   * @return L'entité Depense correspondant.
   */
  public static Facture creationDtoToEntity(@NonNull Long id,
      @NonNull Proprietaire proprietaire, @NonNull String numeroFacture, @NonNull String url) {
    return new Facture(
        id,
        numeroFacture,
        LocalDate.now(),
        1000f,
        proprietaire,
        url
    );
  }

  /**
   * Convertit une entité Depense en un DTO Depense.
   *
   * @param facture L'entité Depense à convertir.
   * @return Le DTO Depense correspondant.
   */
  public static FactureDto entityToDto(@NonNull Facture facture) {
    return new FactureDto(
        facture.getId(),
        facture.getNumeroFacture(),
        facture.getDateFacture().toString(),
        facture.getMontant(),
        facture.getProprietaire().getId(),
        facture.getUrl()
    );
  }

  /**
   * Convertit une liste d'entités Depense en une liste de DTOs Depense.
   *
   * @param depenses La liste des Depense à convertir.
   * @return Une liste de DTOs Depense.
   */
  public static List<FactureDto> entityListToDtoList(
      @NonNull List<Facture> depenses) {
    ArrayList<FactureDto> depenseDtos = new ArrayList<>();
    for (Facture depense : depenses) {
      depenseDtos.add(entityToDto(depense));
    }
    return depenseDtos.stream().toList();
  }

}
