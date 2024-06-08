package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireCreationDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireDto;
import fr.esgi.al5.tayarim.entities.Proprietaire;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.stereotype.Component;



/**
 * Classe de mapping pour convertir entre les DTOs Proprietaire et les entités Proprietaire.
 */
@Component
public class ProprietaireMapper {

  /**
   * Convertit un DTO de création de propriétaire en une entité Proprietaire.
   *
   * @param proprietaireCreationDto Le DTO pour la création d'un propriétaire.
   * @param hashedPassword          Le mot de passe hashé pour le nouveau propriétaire.
   * @return Une entité Proprietaire nouvellement créée.
   */
  public static Proprietaire creationDtoToEntity(
      @NonNull ProprietaireCreationDto proprietaireCreationDto, String hashedPassword) {
    return Proprietaire.builder()
        .nom(proprietaireCreationDto.getNom())
        .prenom(proprietaireCreationDto.getPrenom())
        .email(proprietaireCreationDto.getEmail())
        .numTel(proprietaireCreationDto.getNumTel())
        .motDePasse(hashedPassword)
        .dateInscription(LocalDateTime.now())
        .isPasswordUpdated(false)
        .build();
  }

  /**
   * Convertit une entité Proprietaire en un DTO Proprietaire, incluant éventuellement les logements
   * associés.
   *
   * @param proprietaire L'entité propriétaire à convertir.
   * @param isLogement   Indique si les logements associés doivent être inclus dans le DTO.
   * @return Le DTO Proprietaire.
   */
  public static ProprietaireDto entityToDto(@NonNull Proprietaire proprietaire,
      boolean isLogement) {

    return ProprietaireDto.builder()
        .id(proprietaire.getId())
        .nom(proprietaire.getNom())
        .prenom(proprietaire.getPrenom())
        .email(proprietaire.getEmail())
        .numTel(proprietaire.getNumTel())
        .dateInscription(proprietaire.getDateInscription())
        .isPasswordUpdated(proprietaire.getIsPasswordUpdated())
        .logements(
            isLogement ? LogementMapper.entityListToDtoList(proprietaire.getLogements()) : null)
        .commission(proprietaire.getCommission())
        .build();
  }

  /**
   * Convertit une liste d'entités Proprietaire en une liste de DTOs Proprietaire.
   *
   * @param proprietaireList La liste des propriétaires à convertir.
   * @param isLogement       Indique si les logements associés doivent être inclus dans les DTOs.
   * @return Une liste de DTOs Proprietaire.
   */
  public static List<ProprietaireDto> entityListToDtoList(
      @NonNull List<Proprietaire> proprietaireList, boolean isLogement) {
    return proprietaireList.stream()
        .map(proprietaire -> ProprietaireMapper.entityToDto(proprietaire, isLogement))
        .collect(Collectors.toList());
  }

}
