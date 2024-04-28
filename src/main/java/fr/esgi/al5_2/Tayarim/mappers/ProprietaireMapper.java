package fr.esgi.al5_2.Tayarim.mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import lombok.NonNull;

@Component
public class ProprietaireMapper {

    public static Proprietaire creationDtoToEntity(@NonNull ProprietaireCreationDTO proprietaireCreationDTO){
        return Proprietaire.builder()
                .nom(proprietaireCreationDTO.getNom())
                .prenom(proprietaireCreationDTO.getPrenom())
                .email(proprietaireCreationDTO.getEmail())
                .numTel(proprietaireCreationDTO.getNumTel())
                .motDePasse(proprietaireCreationDTO.getMotDePasse())
                .dateInscription(LocalDateTime.now())
                .build();
    }

    public static ProprietaireDTO entityToDto(@NonNull Proprietaire proprietaire, boolean isLogement){

        return ProprietaireDTO.builder()
                .id(proprietaire.getId())
                .nom(proprietaire.getNom())
                .prenom(proprietaire.getPrenom())
                .email(proprietaire.getEmail())
                .numTel(proprietaire.getNumTel())
                .dateInscription(proprietaire.getDateInscription())
                .logements(isLogement ? LogementMapper.entityListToDtoList(proprietaire.getLogements()) : null).build();
    }

    public static List<ProprietaireDTO> entityListToDtoList(@NonNull List<Proprietaire> proprietaireList, boolean isLogement){
        return proprietaireList.stream()
                .map(proprietaire -> ProprietaireMapper.entityToDto(proprietaire, isLogement))
                .collect(Collectors.toList());
    }

    public static Proprietaire dtoToEntity(@NonNull ProprietaireDTO proprietaireDTO){
        return new Proprietaire(
            proprietaireDTO.getNom(), 
            proprietaireDTO.getPrenom(), 
            proprietaireDTO.getEmail(), 
            proprietaireDTO.getNumTel(), 
            proprietaireDTO.getMotDePasse(), 
            proprietaireDTO.getDateInscription());
    }

}
