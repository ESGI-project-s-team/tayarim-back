package fr.esgi.al5.tayarim.services;

import fr.esgi.al5.tayarim.dto.statistique.StatistiqueDto;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
import fr.esgi.al5.tayarim.repositories.StatistiqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
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
        return new StatistiqueDto(
                getMontantReservationsParMois(year),
                getPrixDepenseParMois(year),
                getDepenseAndReservationParMois(year),
                getNombreReservationParMois(year),
                getTauxOccupationParMois(year)
        );
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

    public List<Float> getDepenseAndReservationParMois(Long year) {
        List<Map<String, Object>> reservationResults = statistiqueRepository.getMontantReservationsParMois(year);
        List<Map<String, Object>> depenseResults = statistiqueRepository.getPrixDepenseParMois(year);

        Map<Integer, Float> reservationMap = new HashMap<>();
        Map<Integer, Float> depenseMap = new HashMap<>();

        // Initialize all months with 0.0
        for (int month = 1; month <= 12; month++) {
            reservationMap.put(month, 0.0f);
            depenseMap.put(month, 0.0f);
        }

        // Populate the reservation map
        for (Map<String, Object> result : reservationResults) {
            Integer month = (Integer) result.get("month");
            Float totalMontant = ((Number) result.get("totalAmount")).floatValue();
            reservationMap.put(month, totalMontant);
        }

        // Populate the depense map
        for (Map<String, Object> result : depenseResults) {
            Integer month = (Integer) result.get("month");
            Float totalPrix = ((Number) result.get("totalPrix")).floatValue();
            depenseMap.put(month, totalPrix);
        }

        // Calculate the difference for each month
        List<Float> differenceParMois = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            float difference = reservationMap.get(month) - depenseMap.get(month);
            differenceParMois.add(difference);
        }

        return differenceParMois;
    }

    public List<Integer> getNombreReservationParMois(Long year) {
        List<Map<String, Object>> rawResults = statistiqueRepository.getNombreReservationParMois(year);
        Map<Integer, Integer> reservationsParMoisMap = new HashMap<>();

        // Initialize all months with 0
        for (int month = 1; month <= 12; month++) {
            reservationsParMoisMap.put(month, 0);
        }

        // Populate the map with actual values from the query result
        for (Map<String, Object> result : rawResults) {
            Integer month = (Integer) result.get("month");
            Integer totalReservations = ((Number) result.get("totalReservations")).intValue();
            reservationsParMoisMap.put(month, totalReservations);
        }

        // Create a list from the map
        List<Integer> reservationsParMois = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            reservationsParMois.add(reservationsParMoisMap.get(month));
        }

        return reservationsParMois;
    }

    public List<Float> getTauxOccupationParMois(Long year) {
        List<Map<String, Object>> rawResults = statistiqueRepository.getOccupiedDaysPerMonth(year);
        Map<Integer, Integer> occupiedDaysPerMonthMap = new HashMap<>();

        // Initialize all months with 0 occupied days
        for (int month = 1; month <= 12; month++) {
            occupiedDaysPerMonthMap.put(month, 0);
        }

        // Calculate the occupied days for each month
        for (Map<String, Object> result : rawResults) {
            Integer month = (Integer) result.get("month");
            Integer startDay = (Integer) result.get("startDay");
            Integer endDay = (Integer) result.get("endDay");

            // Calculate the number of days occupied
            int occupiedDays = endDay - startDay + 1;
            occupiedDaysPerMonthMap.put(month, occupiedDaysPerMonthMap.get(month) + occupiedDays);
        }

        // Calculate the occupancy rate for each month
        List<Float> tauxOccupationParMois = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            int daysInMonth = YearMonth.of(year.intValue(), month).lengthOfMonth();
            float occupancyRate = (occupiedDaysPerMonthMap.get(month) / (float) daysInMonth) * 100;
            occupancyRate = Math.round(occupancyRate * 100.0f) / 100.0f;
            tauxOccupationParMois.add(occupancyRate);
        }

        return tauxOccupationParMois;
    }

}
