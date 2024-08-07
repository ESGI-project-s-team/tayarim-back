package fr.esgi.al5.tayarim.repositories;

import fr.esgi.al5.tayarim.entities.Reservation;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Repository pour les statistiques.
 */
@Repository
public interface StatistiqueRepository extends JpaRepository<Reservation, Long> {

  /**
   * Récupère le montant total des réservations par mois.
   *
   * @param year L'année.
   * @return La liste des montants par mois.
   */
  @Query("SELECT new map(MONTH(r.dateArrivee) as month, SUM(r.montant) as totalAmount) "
      + "FROM RESERVATION r "
      + "WHERE YEAR(r.dateArrivee) = :year AND r.statut = 'done' "
      + "GROUP BY MONTH(r.dateArrivee)")
  List<Map<String, Object>> getMontantReservationsParMois(@Param("year") @NonNull Long year);

  /**
   * Récupère le montant total des réservations par mois pour un propriétaire.
   *
   * @param year           L'année.
   * @param idProprietaire L'identifiant du propriétaire.
   * @return La liste des montants par mois.
   */
  @Query("SELECT new map(MONTH(r.dateArrivee) as month, SUM(r.montant) as totalAmount) "
      + "FROM RESERVATION r "
      + "JOIN r.logement l "
      + "WHERE YEAR(r.dateArrivee) = :year AND r.statut = 'done' "
      + "AND l.proprietaire.id = :idProprietaire "
      + "GROUP BY MONTH(r.dateArrivee)")
  List<Map<String, Object>> getMontantReservationsParMoisByOwner(@Param("year") @NonNull Long year,
      @Param("idProprietaire") @NonNull Long idProprietaire);

  /**
   * Récupère le montant total des dépenses par mois.
   *
   * @param year L'année.
   * @return La liste des montants par mois.
   */
  @Query("SELECT new map(MONTH(d.date) as month, SUM(d.prix) as totalPrix) "
      + "FROM DEPENSE d "
      + "WHERE YEAR(d.date) = :year "
      + "GROUP BY MONTH(d.date)")
  List<Map<String, Object>> getPrixDepenseParMois(@Param("year") @NonNull Long year);

  /**
   * Récupère le montant total des dépenses par mois pour un propriétaire.
   *
   * @param year           L'année.
   * @param idProprietaire L'identifiant du propriétaire.
   * @return La liste des montants par mois.
   */
  @Query("SELECT new map(MONTH(d.date) as month, SUM(d.prix) as totalPrix) "
      + "FROM DEPENSE d "
      + "JOIN d.logement l "
      + "WHERE YEAR(d.date) = :year AND l.proprietaire.id = :idProprietaire "
      + "GROUP BY MONTH(d.date)")
  List<Map<String, Object>> getPrixDepenseParMoiByOwner(@Param("year") @NonNull Long year,
      @Param("idProprietaire") @NonNull Long idProprietaire);

  /**
   * Récupère le nombre total de réservations par mois.
   *
   * @param year L'année.
   * @return La liste des nombres de réservations par mois.
   */
  @Query("SELECT new map(MONTH(r.dateArrivee) as month, COUNT(r.id) as totalReservations) "
      + "FROM RESERVATION r "
      + "WHERE YEAR(r.dateArrivee) = :year AND r.statut = 'done'"
      + "GROUP BY MONTH(r.dateArrivee)")
  List<Map<String, Object>> getNombreReservationParMois(@Param("year") @NonNull Long year);

  /**
   * Récupère le nombre total de réservations par mois pour un propriétaire.
   *
   * @param year           L'année.
   * @param idProprietaire L'identifiant du propriétaire.
   * @return La liste des nombres de réservations par mois.
   */
  @Query("SELECT new map(MONTH(r.dateArrivee) as month, COUNT(r.id) as totalReservations) "
      + "FROM RESERVATION r "
      + "JOIN r.logement l "
      + "WHERE YEAR(r.dateArrivee) = :year AND r.statut = 'done' "
      + "AND l.proprietaire.id = :idProprietaire "
      + "GROUP BY MONTH(r.dateArrivee)")
  List<Map<String, Object>> getNombreReservationParMoisByOwner(@Param("year") @NonNull Long year,
      @Param("idProprietaire") @NonNull Long idProprietaire);

  /**
   * Récupère le nombre de jours occupés par mois.
   *
   * @param year L'année.
   * @return La liste des jours occupés par mois.
   */
  @Query(
      "SELECT new map(MONTH(r.dateArrivee) as month, "
          + "DAY(r.dateArrivee) as startDay, DAY(r.dateDepart) as endDay, "
          + "COUNT(r.id) as reservationCount) "
          + "FROM RESERVATION r "
          + "WHERE YEAR(r.dateArrivee) = :year AND r.statut = 'done' "
          + "GROUP BY MONTH(r.dateArrivee), DAY(r.dateArrivee), DAY(r.dateDepart)")
  List<Map<String, Object>> getOccupiedDaysPerMonth(@Param("year") @NonNull Long year);

  /**
   * Récupère le nombre de jours occupés par mois pour un propriétaire.
   *
   * @param year           L'année.
   * @param idProprietaire L'identifiant du propriétaire.
   * @return La liste des jours occupés par mois.
   */
  @Query(
      "SELECT new map(MONTH(r.dateArrivee) as month, DAY(r.dateArrivee) as startDay, "
          + "DAY(r.dateDepart) as endDay, COUNT(r.id) as reservationCount) "
          + "FROM RESERVATION r "
          + "JOIN r.logement l "
          + "WHERE YEAR(r.dateArrivee) = :year AND r.statut = 'done' "
          + "AND l.proprietaire.id = :idProprietaire "
          + "GROUP BY MONTH(r.dateArrivee), DAY(r.dateArrivee), DAY(r.dateDepart)")
  List<Map<String, Object>> getOccupiedDaysPerMonthByOwner(@Param("year") @NonNull Long year,
      @Param("idProprietaire") @NonNull Long idProprietaire);


}