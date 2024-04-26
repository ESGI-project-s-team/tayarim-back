package fr.esgi.al5_2.Tayarim.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import fr.esgi.al5_2.Tayarim.dto.logement.LogementDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.entities.Logement;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import lombok.NonNull;

@Component
public class LogementMapper {

    public static Logement dtoToEntity(@NonNull LogementDTO logementDTO){
        Proprietaire proprietaire = ProprietaireMapper.dtoToEntity(logementDTO.getProprietaire());
        return new Logement(proprietaire);
    }

    public static LogementDTO entityToDto(@NonNull Logement logement){
        return new LogementDTO(
            logement.getId(), 
            logement.getProprietaire().getId());
    }

    public static List<LogementDTO> entityListToDtoList(@NonNull List<Logement> logements){
        ArrayList<LogementDTO> logementDtos = new ArrayList<>();
        for (Logement logement : logements) {
            logementDtos.add(entityToDto(logement));
        }
        return logementDtos.stream().toList();
    }

}
