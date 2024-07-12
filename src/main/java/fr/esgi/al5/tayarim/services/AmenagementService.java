package fr.esgi.al5.tayarim.services;


import fr.esgi.al5.tayarim.dto.amenagement.AmenagementDto;
import fr.esgi.al5.tayarim.mappers.AmenagementMapper;
import fr.esgi.al5.tayarim.repositories.AmenagementRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe de service gérant les amenagements.
 */
@Service
@Transactional(readOnly = true)
public class AmenagementService {

  private final AmenagementRepository amenagementRepository;

  /**
   * Constructeur pour le service des amenagements.
   *
   * @param amenagementRepository Le repository des amenagements.
   */
  public AmenagementService(AmenagementRepository amenagementRepository) {
    this.amenagementRepository = amenagementRepository;
  }

  /**
   * Récupère tous les amenagements.
   *
   * @return {@link AmenagementDto}
   */
  public List<AmenagementDto> getAllAmenagements() {
    return AmenagementMapper.entityListToDtoList(amenagementRepository.findAll());
  }


}
