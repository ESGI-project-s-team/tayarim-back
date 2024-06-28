package fr.esgi.al5.tayarim.services;


import fr.esgi.al5.tayarim.dto.logement.LogementDto;
import fr.esgi.al5.tayarim.dto.regleslogement.ReglesLogementDto;
import fr.esgi.al5.tayarim.mappers.ReglesLogementMapper;
import fr.esgi.al5.tayarim.repositories.ReglesLogementRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Classe de service gérant les logements.
 */
@Service
@Transactional(readOnly = true)
public class ReglesLogementService {

  private final ReglesLogementRepository reglesLogementRepository;


  /**
   * Constructeur pour le service de regle de logement.
   *
   * @param reglesLogementRepository Le repository des règles de logements.
   */
  public ReglesLogementService(ReglesLogementRepository reglesLogementRepository) {
    this.reglesLogementRepository = reglesLogementRepository;
  }

  /**
   * Tente de récupérer toutes les regles de logements.
   *
   * @return {@link LogementDto}
   */
  public List<ReglesLogementDto> getAllReglesLogements() {
    return ReglesLogementMapper.entityListToDtoList(reglesLogementRepository.findAll());
  }

}
