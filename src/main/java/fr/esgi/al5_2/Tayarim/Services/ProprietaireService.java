package fr.esgi.al5_2.Tayarim.services;

import fr.esgi.al5_2.Tayarim.auth.JwtHelper;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireLoginResponseDTO;
import fr.esgi.al5_2.Tayarim.exceptions.*;
import io.jsonwebtoken.ExpiredJwtException;
import org.mindrot.jbcrypt.BCrypt;
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
    private final JwtHelper jwtHelper;

    public ProprietaireService(ProprietaireRepository proprietaireRepository, JwtHelper jwtHelper) {
        this.proprietaireRepository = proprietaireRepository;
        this.jwtHelper = jwtHelper;
    }

    @Transactional
    public ProprietaireDTO creerProprietaire(@NonNull ProprietaireCreationDTO proprietaireCreationDTO) {
        if (proprietaireRepository.findFirstByEmail(proprietaireCreationDTO.getEmail()).isPresent()){
            throw new ProprietaireEmailAlreadyExistException();
        }

        String numTel = proprietaireCreationDTO.getNumTel();
        numTel = numTel.replaceAll(" ", "");
        if (proprietaireRepository.findFirstByNumTel(numTel).isPresent()){
            throw new ProprietaireNumTelAlreadyExistException();
        }
        proprietaireCreationDTO.setNumTel(numTel);

        String hashedPassword = hashPassword(proprietaireCreationDTO.getMotDePasse());
        if (hashedPassword == null){
            throw new PasswordHashNotPossibleException();
        }

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

    public ProprietaireLoginResponseDTO loginProprietaire(String email, String password){
        System.out.println("FIRST");
        Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findFirstByEmail(email);
        if (optionalProprietaire.isEmpty()){
            throw new ProprietaireNotFoundException();
        }

        Proprietaire proprietaire = optionalProprietaire.get();
        if(!verifyHashedPassword(password, proprietaire.getMotDePasse())){
            throw new ProprietaireNotFoundException();
        }

        String token = jwtHelper.generateToken(email);

        return new ProprietaireLoginResponseDTO(
                proprietaire.getId(),
                token
        );

    }

    public ProprietaireLoginResponseDTO authProprietaire(String token, Long id){

        Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(id);
        if (optionalProprietaire.isEmpty()){
            throw new ProprietaireNotFoundException();
        }

        Proprietaire proprietaire = optionalProprietaire.get();
        if(!jwtHelper.validateToken(token, proprietaire)){
            System.out.println("HERE ?");
            throw new TokenExpireOrInvalidException();
        }

        return new ProprietaireLoginResponseDTO(
                proprietaire.getId(),
                token
        );

    }

    private String hashPassword(@NonNull String password){
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        if(!verifyHashedPassword(password, hashedPassword)){
            return null;
        }

        return hashedPassword;
    }

    private boolean verifyHashedPassword(@NonNull String password, @NonNull String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);

    }
}
