package fr.esgi.al5.tayarim.repositories;

import fr.esgi.al5.tayarim.entities.Notification;
import fr.esgi.al5.tayarim.entities.TypeLogement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Interface de repository pour les opérations de base de données sur les entités Logement.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findAllByUtilisateurId(Long idUser);

}
