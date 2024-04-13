package fr.esgi.al5_2.Tayarim.Services;

import fr.esgi.al5_2.Tayarim.exceptions.ProprietaireEmailAlreadyExistException;
import fr.esgi.al5_2.Tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5_2.Tayarim.exceptions.ProprietaireNumTelAlreadyExistException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireCreationDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import fr.esgi.al5_2.Tayarim.mappers.ProprietaireMapper;
import fr.esgi.al5_2.Tayarim.repositories.ProprietaireRepository;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProprietaireService {

    private final ProprietaireRepository proprietaireRepository;
    private final PasswordEncoder passwordEncoder;

    public ProprietaireService(ProprietaireRepository proprietaireRepository, PasswordEncoder passwordEncoder) {
        this.proprietaireRepository = proprietaireRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ProprietaireDTO creerProprietaire(@NonNull ProprietaireCreationDTO proprietaireCreationDTO) {

        if (proprietaireRepository.findFirstByEmail(proprietaireCreationDTO.getEmail()) != null){
            throw new ProprietaireEmailAlreadyExistException();
        }

        String numTel = proprietaireCreationDTO.getNumTel();
        numTel = numTel.replaceAll(" ", "");
        if (proprietaireRepository.findFirstByNumTel(numTel) != null){
            throw new ProprietaireNumTelAlreadyExistException();
        }
        proprietaireCreationDTO.setNumTel(numTel);

        String hashedPassword = passwordEncoder.encode(proprietaireCreationDTO.getMotDePasse());
        proprietaireCreationDTO.setMotDePasse(hashedPassword);

        Proprietaire proprietaire = ProprietaireMapper.creationDtoToEntity(proprietaireCreationDTO);

        return ProprietaireMapper.entityToDto(proprietaireRepository.save(proprietaire), false);
    }

    public List<ProprietaireDTO> getProprietaire(boolean isLogement){
        return ProprietaireMapper.entityListToDtoList(proprietaireRepository.findAll(), isLogement);
    }

    public ProprietaireDTO getProprietaireById(Long id, boolean isLogement){

        Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(id);
        if(optionalProprietaire.isEmpty()){
            throw new ProprietaireNotFoundException();
        }
        return ProprietaireMapper.entityToDto(optionalProprietaire.get(), isLogement);
    }
}
