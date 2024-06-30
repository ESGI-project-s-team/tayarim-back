package fr.esgi.al5.tayarim.services;

import fr.esgi.al5.tayarim.dto.statistique.StatistiqueDto;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
import fr.esgi.al5.tayarim.repositories.StatistiqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class StatistiqueService {
    private final StatistiqueRepository statistiqueRepository;

    public StatistiqueService(StatistiqueRepository statistiqueRepository) {
        this.statistiqueRepository = statistiqueRepository;
    }

    public StatistiqueDto getStatistiqueParAnnee(Long year) {
        /*return new StatistiqueDto(
                getMontantReservationsParMois(year),
                reservationRepository.getDepenseParMois(year),
                reservationRepository.getMontantConciergerieParMois(year),
                reservationRepository.getNombreReservationParMois(year),
                reservationRepository.getTauxOccupationParMois(year)
        );*/
        System.out.println(getMontantReservationsParMois(year));
        System.out.println(getPrixDepenseParMois(year));
        return null;
    }


    public List<Float> getMontantReservationsParMois(Long year) {
        List<Map<String, Object>> rawResults = statistiqueRepository.getMontantReservationsParMois(year);
        Map<Integer, Float> montantParMoisMap = new HashMap<>();

        // Initialize all months with 0.0
        for (int month = 1; month <= 12; month++) {
            montantParMoisMap.put(month, 0.0f);
        }

        // Populate the map with actual values from the query result
        for (Map<String, Object> result : rawResults) {
            Integer month = (Integer) result.get("month");
            Float totalAmount = ((Number) result.get("totalAmount")).floatValue();
            montantParMoisMap.put(month, totalAmount);
        }

        // Create a list from the map
        List<Float> montantParMois = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            montantParMois.add(montantParMoisMap.get(month));
        }

        return montantParMois;
    }

    public List<Float> getPrixDepenseParMois(Long year) {
        List<Map<String, Object>> rawResults = statistiqueRepository.getPrixDepenseParMois(year);
        Map<Integer, Float> prixParMoisMap = new HashMap<>();

        // Initialize all months with 0.0
        for (int month = 1; month <= 12; month++) {
            prixParMoisMap.put(month, 0.0f);
        }

        // Populate the map with actual values from the query result
        for (Map<String, Object> result : rawResults) {
            Integer month = (Integer) result.get("month");
            Float totalPrix = ((Number) result.get("totalPrix")).floatValue();
            prixParMoisMap.put(month, totalPrix);
        }

        // Create a list from the map
        List<Float> prixParMois = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            prixParMois.add(prixParMoisMap.get(month));
        }

        return prixParMois;
    }
}
