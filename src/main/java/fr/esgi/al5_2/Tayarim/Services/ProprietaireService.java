package fr.esgi.al5_2.Tayarim.Services;

import org.springframework.stereotype.Service;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import fr.esgi.al5_2.Tayarim.mappers.ProprietaireMapper;
import fr.esgi.al5_2.Tayarim.repositories.ProprietaireRepository;
import lombok.NonNull;

@Service
public class ProprietaireService {

    private final ProprietaireRepository proprietaireRepository;

    public ProprietaireService(ProprietaireRepository proprietaireRepository) {
        this.proprietaireRepository = proprietaireRepository;
    }

    public ProprietaireDTO creerProprietaire(@NonNull ProprietaireCreationDTO proprietaireCreationDTO, boolean isLogement) {
        
        Proprietaire proprietaire = ProprietaireMapper.creationDtoToEntity(proprietaireCreationDTO);

        return ProprietaireMapper.entityToDto(proprietaireRepository.save(proprietaire), isLogement);
    }
}
