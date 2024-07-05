package fr.esgi.al5.tayarim.services;

import fr.esgi.al5.tayarim.dto.statistique.StatistiqueDto;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.repositories.ReservationRepository;
import fr.esgi.al5.tayarim.repositories.StatistiqueRepository;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service pour les statistiques.
 */
@Service
@Transactional(readOnly = true)
public class StatistiqueService {

  private final StatistiqueRepository statistiqueRepository;

  /**
   * Constructeur de la classe.
   *
   * @param statistiqueRepository le repository des statistiques
   */
  public StatistiqueService(StatistiqueRepository statistiqueRepository) {
    this.statistiqueRepository = statistiqueRepository;
  }

  /**
   * Récupère les statistiques pour une année donnée.
   *
   * @param year           L'année pour laquelle on veut les statistiques.
   * @param isAdmin        Si l'utilisateur est un admin.
   * @param idProprietaire L'id du propriétaire.
   * @return Les statistiques pour l'année donnée.
   */
  @Transactional
  public StatistiqueDto getStatistiqueParAnnee(Long year, @NonNull Boolean isAdmin,
      @NonNull Long idProprietaire) {

    return new StatistiqueDto(
        getMontantReservationsParMois(year, isAdmin, idProprietaire),
        getPrixDepenseParMois(year, isAdmin, idProprietaire),
        getDepenseAndReservationParMois(year, isAdmin, idProprietaire),
        getNombreReservationParMois(year, isAdmin, idProprietaire),
        getTauxOccupationParMois(year, isAdmin, idProprietaire)
    );

  }

  /**
   * Récupère le montant des réservations par mois pour une année donnée.
   *
   * @param year           L'année pour laquelle on veut les statistiques.
   * @param isAdmin        Si l'utilisateur est un admin.
   * @param idProprietaire L'id du propriétaire.
   * @return Le montant des réservations par mois pour l'année donnée.
   */
  public List<Float> getMontantReservationsParMois(Long year, Boolean isAdmin,
      Long idProprietaire) {
    List<Map<String, Object>> rawResults;
    if (isAdmin) {
      rawResults = statistiqueRepository.getMontantReservationsParMois(year);
    } else {
      rawResults = statistiqueRepository.getMontantReservationsParMoisByOwner(year, idProprietaire);
    }
    Map<Integer, Float> montantParMoisMap = new HashMap<>();

    for (int month = 1; month <= 12; month++) {
      montantParMoisMap.put(month, 0.0f);
    }

    for (Map<String, Object> result : rawResults) {
      Integer month = (Integer) result.get("month");
      Float totalAmount = ((Number) result.get("totalAmount")).floatValue();
      montantParMoisMap.put(month, totalAmount);
    }

    List<Float> montantParMois = new ArrayList<>();
    for (int month = 1; month <= 12; month++) {
      montantParMois.add(montantParMoisMap.get(month));
    }

    return montantParMois;
  }

  /**
   * Récupère le prix des dépenses par mois pour une année donnée.
   *
   * @param year           L'année pour laquelle on veut les statistiques.
   * @param isAdmin        Si l'utilisateur est un admin.
   * @param idProprietaire L'id du propriétaire.
   * @return Le prix des dépenses par mois pour l'année donnée.
   */
  public List<Float> getPrixDepenseParMois(Long year, Boolean isAdmin, Long idProprietaire) {
    List<Map<String, Object>> rawResults;
    if (isAdmin) {
      rawResults = statistiqueRepository.getPrixDepenseParMois(year);
    } else {
      rawResults = statistiqueRepository.getPrixDepenseParMoiByOwner(year, idProprietaire);
    }
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

  /**
   * Récupère la différence entre le montant des réservations et le prix des dépenses par mois pour
   * une année donnée.
   *
   * @param year           L'année pour laquelle on veut les statistiques.
   * @param isAdmin        Si l'utilisateur est un admin.
   * @param idProprietaire L'id du propriétaire.
   * @return La différence entre le montant des réservations et le prix des dépenses par mois pour
   *     l'année donnée.
   */
  public List<Float> getDepenseAndReservationParMois(Long year, Boolean isAdmin,
      Long idProprietaire) {
    List<Map<String, Object>> reservationResults;
    List<Map<String, Object>> depenseResults;

    if (isAdmin) {
      reservationResults = statistiqueRepository.getMontantReservationsParMois(year);
      depenseResults = statistiqueRepository.getPrixDepenseParMois(year);
    } else {
      reservationResults = statistiqueRepository.getMontantReservationsParMoisByOwner(year,
          idProprietaire);
      depenseResults = statistiqueRepository.getPrixDepenseParMoiByOwner(year, idProprietaire);
    }

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

  /**
   * Récupère le nombre de réservations par mois pour une année donnée.
   *
   * @param year           L'année pour laquelle on veut les statistiques.
   * @param isAdmin        Si l'utilisateur est un admin.
   * @param idProprietaire L'id du propriétaire.
   * @return Le nombre de réservations par mois pour l'année donnée.
   */
  public List<Integer> getNombreReservationParMois(Long year, Boolean isAdmin,
      Long idProprietaire) {

    List<Map<String, Object>> rawResults;
    if (isAdmin) {
      rawResults = statistiqueRepository.getNombreReservationParMois(year);
    } else {
      rawResults = statistiqueRepository.getNombreReservationParMoisByOwner(year, idProprietaire);
    }

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

  /**
   * Récupère le taux d'occupation par mois pour une année donnée.
   *
   * @param year           L'année pour laquelle on veut les statistiques.
   * @param isAdmin        Si l'utilisateur est un admin.
   * @param idProprietaire L'id du propriétaire.
   * @return Le taux d'occupation par mois pour l'année donnée.
   */
  public List<Float> getTauxOccupationParMois(Long year, Boolean isAdmin, Long idProprietaire) {
    List<Map<String, Object>> rawResults;
    if (isAdmin) {
      rawResults = statistiqueRepository.getOccupiedDaysPerMonth(year);
    } else {
      rawResults = statistiqueRepository.getOccupiedDaysPerMonthByOwner(year, idProprietaire);
    }
    Map<Integer, Integer> occupiedDaysPerMonthMap = new HashMap<>();

    // Initialize all months with 0 occupied days
    for (int month = 1; month <= 12; month++) {
      occupiedDaysPerMonthMap.put(month, 0);
    }

    // Calculate the occupied days for each month
    for (Map<String, Object> result : rawResults) {
      Integer startMonth = (Integer) result.get("month");
      Integer startDay = (Integer) result.get("startDay");
      Integer endDay = (Integer) result.get("endDay");

      YearMonth startYearMonth = YearMonth.of(year.intValue(), startMonth);
      int daysInStartMonth = startYearMonth.lengthOfMonth();

      if (startDay <= endDay) {
        // The reservation is within the same month
        int occupiedDays = endDay - startDay + 1;
        occupiedDaysPerMonthMap.put(startMonth,
            occupiedDaysPerMonthMap.get(startMonth) + occupiedDays);
      } else {
        // The reservation spans to the next month
        int occupiedDaysInStartMonth = daysInStartMonth - startDay + 1;
        occupiedDaysPerMonthMap.put(startMonth,
            occupiedDaysPerMonthMap.get(startMonth) + occupiedDaysInStartMonth);

        int nextMonth = startMonth + 1;
        if (nextMonth > 12) {
          nextMonth = 1; // Wrap around to January
        }
        occupiedDaysPerMonthMap.put(nextMonth, occupiedDaysPerMonthMap.get(nextMonth) + endDay);
      }
    }

    // Calculate the occupancy rate for each month
    List<Float> tauxOccupationParMois = new ArrayList<>();
    for (int month = 1; month <= 12; month++) {
      int daysInMonth = YearMonth.of(year.intValue(), month).lengthOfMonth();
      float occupancyRate = (occupiedDaysPerMonthMap.get(month) / (float) daysInMonth) * 100;
      occupancyRate = Math.min(Math.round(occupancyRate * 100.0f) / 100.0f,
          100.0f); // Ensure occupancy rate does not exceed 100%
      tauxOccupationParMois.add(occupancyRate);
    }

    //if value is > 100, set it to 100 or 0 if it is < 0
    for (int i = 0; i < tauxOccupationParMois.size(); i++) {
      if (tauxOccupationParMois.get(i) > 100) {
        tauxOccupationParMois.set(i, 100.0f);
      } else if (tauxOccupationParMois.get(i) < 0) {
        tauxOccupationParMois.set(i, 0.0f);
      }
    }

    return tauxOccupationParMois;
  }

}
