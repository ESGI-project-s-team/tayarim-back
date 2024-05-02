package fr.esgi.al5_2.Tayarim.mappers;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.AdministrateurCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.AdministrateurDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.entities.Administrateur;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Fournit des méthodes statiques pour mapper entre les entités Administrateur et les DTOs
 * associés.
 */
@Component
public class AdministrateurMapper {

  /**
   * Convertit un DTO de création d'administrateur en une entité administrateur.
   *
   * @param administrateurCreationDTO Le DTO contenant les informations pour créer un
   *                                  administrateur.
   * @return Une entité Administrateur prête à être persistée.
   */
  public static Administrateur creationDtoToEntity(
      @NonNull AdministrateurCreationDTO administrateurCreationDTO) {
    return Administrateur.builder()
        .nom(administrateurCreationDTO.getNom())
        .prenom(administrateurCreationDTO.getPrenom())
        .email(administrateurCreationDTO.getEmail())
        .numTel(administrateurCreationDTO.getNumTel())
        .motDePasse(administrateurCreationDTO.getMotDePasse())
        .build();
  }

  /**
   * Convertit une entité Administrateur en un DTO Administrateur.
   *
   * @param administrateur L'entité administrateur à convertir.
   * @return Le DTO représentant l'administrateur.
   */
  public static AdministrateurDTO entityToDto(@NonNull Administrateur administrateur) {

    return AdministrateurDTO.builder()
        .id(administrateur.getId())
        .nom(administrateur.getNom())
        .prenom(administrateur.getPrenom())
        .email(administrateur.getEmail())
        .numTel(administrateur.getNumTel())
        .build();
  }

  /**
   * Convertit une liste d'entités Administrateur en une liste de DTOs Administrateur.
   *
   * @param administrateurList La liste des entités à convertir.
   * @return Une liste de DTOs.
   */
  public static List<AdministrateurDTO> entityListToDtoList(
      @NonNull List<Administrateur> administrateurList) {
    return administrateurList.stream()
        .map(AdministrateurMapper::entityToDto)
        .collect(Collectors.toList());
  }

}
