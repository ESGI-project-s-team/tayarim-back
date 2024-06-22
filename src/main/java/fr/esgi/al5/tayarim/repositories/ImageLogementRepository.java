package fr.esgi.al5.tayarim.repositories;

import fr.esgi.al5.tayarim.entities.ImageLogement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 * Interface de repository pour les opérations de base de données sur les entités ImageLogement.
 */
@Repository
public interface ImageLogementRepository extends JpaRepository<ImageLogement, Long> {

  @Modifying
  @Transactional
  @Query("DELETE "
      + "FROM IMAGELOGEMENT i "
      + "WHERE i.logement.id = :idLogement "
      + "AND i.id NOT IN (:ids)")
  int deleteUnusedImage(@Param("idLogement") Long idLogement, @Param("ids") List<Long> ids);

}
