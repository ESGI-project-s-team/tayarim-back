package fr.esgi.al5.tayarim.repositories;

import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import fr.esgi.al5.tayarim.entities.Reservation;
import fr.esgi.al5.tayarim.entities.TypeLogement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * Interface de repository pour les opérations de base de données sur les entités Logement.
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  Optional<Reservation> findByIdCommande(@NonNull String idCommande);

  @Query("SELECT r FROM RESERVATION r "
      + "JOIN r.logement l "
      + "JOIN l.proprietaire p "
      + "WHERE p.id = :idProprietaire")
  List<Reservation> findAllByIdProprietaire(@Param("idProprietaire") @NonNull Long idProprietaire);

  List<Reservation> findAllByLogementId(@NonNull Long idLogement);

  List<Reservation> findAllByLogementIdAndStatutIn(@NonNull Long idLogement,
      @NonNull List<String> statuts);

  @Query("SELECT r FROM RESERVATION r "
      + "JOIN r.logement l "
      + "JOIN l.typeLogement t "
      + "WHERE l.id = :idLogement "
      + "AND r.statut IN :status "
      + "AND r.dateDepart BETWEEN :startDate AND :endDate ")
  List<Reservation> findAllByLogementIdAndStatutInAndDateDepartStartsWith(
      @Param("idLogement") Long idLogement,
      @Param("status") Collection<String> status, @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}
