package fr.esgi.al5.tayarim.repositories;

import fr.esgi.al5.tayarim.entities.ReglesLogement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Interface de repository pour les opérations de base de données sur les entités Logement.
 */
@Repository
public interface ReglesLogementRepository extends JpaRepository<ReglesLogement, Long> {

}
