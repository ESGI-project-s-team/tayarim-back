package fr.esgi.al5.tayarim.mappers;

import fr.esgi.al5.tayarim.dto.proprietaire.AdministrateurCreationDto;
import fr.esgi.al5.tayarim.dto.proprietaire.AdministrateurDto;
import fr.esgi.al5.tayarim.entities.Administrateur;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.stereotype.Component;



/**
 * Fournit des méthodes statiques pour mapper entre les entités Administrateur et les DTOs
 * associés.
 */
@Component
public class AdministrateurMapper {

  /**
   * Convertit un DTO de création d'administrateur en une entité administrateur.
   *
   * @param administrateurCreationDto Le DTO contenant les informations pour créer un
   *                                  administrateur.
   * @return Une entité Administrateur prête à être persistée.
   */
  public static Administrateur creationDtoToEntity(
      @NonNull AdministrateurCreationDto administrateurCreationDto) {
    return Administrateur.builder()
        .nom(administrateurCreationDto.getNom())
        .prenom(administrateurCreationDto.getPrenom())
        .email(administrateurCreationDto.getEmail())
        .numTel(administrateurCreationDto.getNumTel())
        .motDePasse(administrateurCreationDto.getMotDePasse())
        .build();
  }

  /**
   * Convertit une entité Administrateur en un DTO Administrateur.
   *
   * @param administrateur L'entité administrateur à convertir.
   * @return Le DTO représentant l'administrateur.
   */
  public static AdministrateurDto entityToDto(@NonNull Administrateur administrateur) {

    return AdministrateurDto.builder()
        .id(administrateur.getId())
        .nom(administrateur.getNom())
        .prenom(administrateur.getPrenom())
        .email(administrateur.getEmail())
        .numTel(administrateur.getNumTel())
        .lang(administrateur.getLanguage())
        .build();
  }

  /**
   * Convertit une liste d'entités Administrateur en une liste de DTOs Administrateur.
   *
   * @param administrateurList La liste des entités à convertir.
   * @return Une liste de DTOs.
   */
  public static List<AdministrateurDto> entityListToDtoList(
      @NonNull List<Administrateur> administrateurList) {
    return administrateurList.stream()
        .map(AdministrateurMapper::entityToDto)
        .collect(Collectors.toList());
  }

}
