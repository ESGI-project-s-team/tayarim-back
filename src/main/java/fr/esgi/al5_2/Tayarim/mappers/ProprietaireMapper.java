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
        return new Proprietaire(
            proprietaireCreationDTO.getNom(), 
            proprietaireCreationDTO.getPrenom(), 
            proprietaireCreationDTO.getEmail(), 
            proprietaireCreationDTO.getNumTel(), 
            proprietaireCreationDTO.getMotDePasse(),
            LocalDateTime.now());
    }

    public static ProprietaireDTO entityToDto(@NonNull Proprietaire proprietaire, boolean isLogement){
        return new ProprietaireDTO(
            proprietaire.getId(),
            proprietaire.getNom(),
            proprietaire.getPrenom(),
            proprietaire.getEmail(),
            proprietaire.getNumTel(),
            proprietaire.getDateInscription(),
            isLogement ? LogementMapper.entityListToDtoList(proprietaire.getLogements()) : null);
    }

    public static List<ProprietaireDTO> entityListToDtoList(@NonNull List<Proprietaire> proprietaireList, boolean isLogement){
        return proprietaireList.stream()
                .map(proprietaire -> ProprietaireMapper.entityToDto(proprietaire, isLogement))
                .collect(Collectors.toList());
    }

}
