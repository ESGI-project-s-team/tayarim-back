package fr.esgi.al5.tayarim.repositories;

import fr.esgi.al5.tayarim.entities.Facture;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Interface de repository pour les opérations de base de données sur les entités Facture.
 */
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {

  List<Facture> findAllByProprietaireId(Long proprietaireId);

  Optional<Facture> findByNumeroFacture(String numeroFacture);

  @Query(value = "SELECT MAX(f.id) FROM FACTURE f")
  Integer getMaximumFactureId();

}
