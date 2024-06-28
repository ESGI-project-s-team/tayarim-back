package fr.esgi.al5.tayarim.repositories;

import fr.esgi.al5.tayarim.entities.Indisponibilite;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Interface de repository pour les opérations de base de données sur les entités Indisponibilite.
 */
@Repository
public interface IndisponibiliteRepository extends JpaRepository<Indisponibilite, Long> {

  List<Indisponibilite> findAllByLogementId(Long idLogement);

}
