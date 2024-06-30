package fr.esgi.al5.tayarim.dto.statistique;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Dto de statistique.
 */
@Data
@AllArgsConstructor
public class StatistiqueDto {

  private List<Float> montantReservationsParMois;
  private List<Float> depenseParMois;
  private List<Float> montantReservationAndDepenseParMois;
  private List<Integer> nombreReservationParMois;
  private List<Float> tauxOccupationParMois;
}
