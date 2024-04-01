package fr.esgi.al5_2.Tayarim.mappers;

import java.time.LocalDateTime;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import lombok.NonNull;

public class ProprietaireMapper {

    public Proprietaire creationDtoToEntity(@NonNull ProprietaireCreationDTO proprietaireCreationDTO){
        return new Proprietaire(
            proprietaireCreationDTO.getNom(), 
            proprietaireCreationDTO.getPrenom(), 
            proprietaireCreationDTO.getEmail(), 
            proprietaireCreationDTO.getNumTel(), 
            proprietaireCreationDTO.getMotDePasse(),
            LocalDateTime.now());
    }

    public ProprietaireDTO entityToDto(@NonNull Proprietaire proprietaire){
        return new ProprietaireDTO(
            proprietaire.getId(),
            proprietaire.getNom(),
            proprietaire.getPrenom(),
            proprietaire.getEmail(),
            proprietaire.getNumTel(),
            proprietaire.getDateInscription());
    }

}
