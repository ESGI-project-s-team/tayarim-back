package fr.esgi.al5.tayarim.repositories;

import fr.esgi.al5.tayarim.entities.Depense;
import fr.esgi.al5.tayarim.entities.TypeLogement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Interface de repository pour les opérations de base de données sur les entités Depense.
 */
@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long> {

  List<Depense> findAllByLogementIdAndDateBetween(Long idLogment, LocalDate start, LocalDate end);

  List<Depense> findAllByLogementProprietaireId(Long idProprietaire);

}
