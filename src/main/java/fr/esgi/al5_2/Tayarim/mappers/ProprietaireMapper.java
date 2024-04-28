package fr.esgi.al5_2.Tayarim.mappers;

import java.time.LocalDateTime;

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

    public static ProprietaireDTO entityToDto(@NonNull Proprietaire proprietaire, boolean isLogements){
        return new ProprietaireDTO(
            proprietaire.getId(),
            proprietaire.getNom(),
            proprietaire.getPrenom(),
            proprietaire.getEmail(),
            proprietaire.getNumTel(),
            null,
            proprietaire.getDateInscription(), 
            isLogements ? LogementMapper.entityListToDtoList(proprietaire.getLogements()) : null);
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
