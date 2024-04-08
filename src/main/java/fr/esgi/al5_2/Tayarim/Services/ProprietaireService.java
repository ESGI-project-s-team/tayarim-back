package fr.esgi.al5_2.Tayarim.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import fr.esgi.al5_2.Tayarim.exceptions.ProprietaireNullException;
import fr.esgi.al5_2.Tayarim.mappers.ProprietaireMapper;
import fr.esgi.al5_2.Tayarim.repositories.ProprietaireRepository;
import jakarta.validation.Validator;
import lombok.NonNull;

@Service
public class ProprietaireService {

    private ProprietaireMapper proprietaireMapper = new ProprietaireMapper();

    @Autowired
    private ProprietaireRepository proprietaireRepository;

    @Autowired
	private Validator validator;

    public ProprietaireDTO creeProprietaire(@NonNull ProprietaireCreationDTO proprietaireCreationDTO, boolean isLogement) {
        
        Proprietaire proprietaire = proprietaireMapper.creationDtoToEntity(proprietaireCreationDTO);
        
        if(proprietaire == null){
            throw new ProprietaireNullException();
        }

        return proprietaireMapper.entityToDto(proprietaireRepository.save(proprietaire), isLogement);
    }
}
