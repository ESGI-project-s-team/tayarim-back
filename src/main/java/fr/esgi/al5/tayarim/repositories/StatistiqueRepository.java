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
 * Interface de repository pour les opérations de base de données sur les entités Statistique.
 */
@Repository
public interface StatistiqueRepository extends JpaRepository<Reservation, Long> {

  @Query("SELECT new map(MONTH(r.dateReservation) as month, SUM(r.montant) as totalAmount) "
      + "FROM RESERVATION r "
      + "WHERE YEAR(r.dateReservation) = :year AND r.statut = 'done' "
      + "GROUP BY MONTH(r.dateReservation)")
  List<Map<String, Object>> getMontantReservationsParMois(@Param("year") @NonNull Long year);

  @Query("SELECT new map(MONTH(r.dateReservation) as month, SUM(r.montant) as totalAmount) "
      + "FROM RESERVATION r "
      + "JOIN r.logement l "
      + "WHERE YEAR(r.dateReservation) = :year AND r.statut = 'done' "
      + "AND l.proprietaire.id = :idProprietaire "
      + "GROUP BY MONTH(r.dateReservation)")
  List<Map<String, Object>> getMontantReservationsParMoisByOwner(@Param("year") @NonNull Long year,
      @Param("idProprietaire") @NonNull Long idProprietaire);


  @Query("SELECT new map(MONTH(d.date) as month, SUM(d.prix) as totalPrix) "
      + "FROM DEPENSE d "
      + "WHERE YEAR(d.date) = :year "
      + "GROUP BY MONTH(d.date)")
  List<Map<String, Object>> getPrixDepenseParMois(@Param("year") @NonNull Long year);

  @Query("SELECT new map(MONTH(d.date) as month, SUM(d.prix) as totalPrix) "
      + "FROM DEPENSE d "
      + "JOIN d.logement l "
      + "WHERE YEAR(d.date) = :year AND l.proprietaire.id = :idProprietaire "
      + "GROUP BY MONTH(d.date)")
  List<Map<String, Object>> getPrixDepenseParMoiByOwner(@Param("year") @NonNull Long year,
      @Param("idProprietaire") @NonNull Long idProprietaire);

  @Query("SELECT new map(MONTH(r.dateReservation) as month, COUNT(r.id) as totalReservations) "
      + "FROM RESERVATION r "
      + "WHERE YEAR(r.dateReservation) = :year AND r.statut = 'done'"
      + "GROUP BY MONTH(r.dateReservation)")
  List<Map<String, Object>> getNombreReservationParMois(@Param("year") @NonNull Long year);

  @Query("SELECT new map(MONTH(r.dateReservation) as month, COUNT(r.id) as totalReservations) "
      + "FROM RESERVATION r "
      + "JOIN r.logement l "
      + "WHERE YEAR(r.dateReservation) = :year AND r.statut = 'done' "
      + "AND l.proprietaire.id = :idProprietaire "
      + "GROUP BY MONTH(r.dateReservation)")
  List<Map<String, Object>> getNombreReservationParMoisByOwner(@Param("year") @NonNull Long year,
      @Param("idProprietaire") @NonNull Long idProprietaire);

  @Query(
      "SELECT new map(MONTH(r.dateArrivee) as month, DAY(r.dateArrivee) as startDay, "
          + "DAY(r.dateDepart) as endDay, COUNT(r.id) as reservationCount) "
          + "FROM RESERVATION r "
          + "WHERE YEAR(r.dateArrivee) = :year AND r.statut = 'done' "
          + "GROUP BY MONTH(r.dateArrivee), DAY(r.dateArrivee), DAY(r.dateDepart)")
  List<Map<String, Object>> getOccupiedDaysPerMonth(@Param("year") @NonNull Long year);

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
