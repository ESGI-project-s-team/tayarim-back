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

@Component
public class AdministrateurMapper {

    public static Administrateur creationDtoToEntity(@NonNull AdministrateurCreationDTO administrateurCreationDTO){
        return Administrateur.builder()
                .nom(administrateurCreationDTO.getNom())
                .prenom(administrateurCreationDTO.getPrenom())
                .email(administrateurCreationDTO.getEmail())
                .numTel(administrateurCreationDTO.getNumTel())
                .motDePasse(administrateurCreationDTO.getMotDePasse())
                .build();
    }

    public static AdministrateurDTO entityToDto(@NonNull Administrateur administrateur){

        return AdministrateurDTO.builder()
                .id(administrateur.getId())
                .nom(administrateur.getNom())
                .prenom(administrateur.getPrenom())
                .email(administrateur.getEmail())
                .numTel(administrateur.getNumTel())
                .build();
        }

    public static List<AdministrateurDTO> entityListToDtoList(@NonNull List<Administrateur> administrateurList){
        return administrateurList.stream()
                .map(AdministrateurMapper::entityToDto)
                .collect(Collectors.toList());
    }

}
