package fr.esgi.al5.tayarim.repositories;

import fr.esgi.al5.tayarim.entities.Logement;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import java.util.List;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Interface de repository pour les opérations de base de données sur les entités Logement.
 */
@Repository
public interface LogementRepository extends JpaRepository<Logement, Long> {

  @Query("SELECT l FROM LOGEMENT l "
      + "LEFT JOIN FETCH l.reglesLogements "
      + "LEFT JOIN FETCH l.amenagements")
  @NonNull
  List<Logement> findAll();

  List<Logement> findAllByProprietaire(@NonNull Proprietaire proprietaire);

}
