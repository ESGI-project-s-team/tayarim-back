package fr.esgi.al5.tayarim.repositories;

import fr.esgi.al5.tayarim.entities.Amenagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Interface de repository pour les opérations de base de données sur les entités Amenagement.
 */
@Repository
public interface AmenagementRepository extends JpaRepository<Amenagement, Long> {

}
