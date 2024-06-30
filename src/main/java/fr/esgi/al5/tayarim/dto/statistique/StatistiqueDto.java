package fr.esgi.al5.tayarim.dto.statistique;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StatistiqueDto {
    private List<Float> montantReservationsParMois;
    private List<Float> depenseParMois;
    private List<Float> montantReservationAndDepenseParMois;
    private List<Integer> nombreReservationParMois;
    private List<Float> tauxOccupationParMois;
}
