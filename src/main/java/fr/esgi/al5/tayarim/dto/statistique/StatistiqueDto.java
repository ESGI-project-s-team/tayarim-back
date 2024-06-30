package fr.esgi.al5.tayarim.dto.statistique;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StatistiqueDto {
    private List<Long> montantReservationsParMois;
    private List<Long> depenseParMois;
    private List<Long> montantConciergerieParMois;
    private List<Long> nombreReservationParMois;
    private List<Long> tauxOccupationParMois;
}
