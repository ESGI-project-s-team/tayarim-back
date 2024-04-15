package fr.esgi.al5_2.Tayarim.services;

import fr.esgi.al5_2.Tayarim.auth.JwtHelper;
import fr.esgi.al5_2.Tayarim.auth.TokenCacheService;
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
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProprietaireService {

    private final ProprietaireRepository proprietaireRepository;
    private final JwtHelper jwtHelper;

    private final TokenCacheService tokenCacheService;

    public ProprietaireService(ProprietaireRepository proprietaireRepository, JwtHelper jwtHelper, TokenCacheService tokenCacheService) {
        this.proprietaireRepository = proprietaireRepository;
        this.jwtHelper = jwtHelper;
        this.tokenCacheService = tokenCacheService;
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
        Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findFirstByEmail(email);
        if (optionalProprietaire.isEmpty()){
            throw new ProprietaireNotFoundException();
        }

        Proprietaire proprietaire = optionalProprietaire.get();
        if(!verifyHashedPassword(password, proprietaire.getMotDePasse())){
            throw new ProprietaireNotFoundException();
        }

        String uuid = tokenCacheService.getFromCache(proprietaire.getId());
        if (uuid == null){
            uuid = UUID.randomUUID().toString();
            tokenCacheService.addToCache(proprietaire.getId() ,uuid);
        }

        String token = jwtHelper.generateToken(email, uuid);

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

        String uuid = tokenCacheService.getFromCache(proprietaire.getId());
        if(uuid == null){
            throw new TokenExpireOrInvalidException();
        }

        if(!jwtHelper.validateToken(token, proprietaire.getEmail(), uuid)){
            throw new TokenExpireOrInvalidException();
        }

        return new ProprietaireLoginResponseDTO(
                proprietaire.getId(),
                token
        );

    }

    public void logoutProprietaire(String token, Long id){

        Optional<Proprietaire> optionalProprietaire = proprietaireRepository.findById(id);
        if (optionalProprietaire.isEmpty()){
            throw new ProprietaireNotFoundException();
        }

        Proprietaire proprietaire = optionalProprietaire.get();

        String uuid = tokenCacheService.getFromCache(proprietaire.getId());
        if(uuid == null){
            throw new TokenExpireOrInvalidException();
        }

        if(!jwtHelper.validateToken(token, proprietaire.getEmail(), uuid)){
            throw new TokenExpireOrInvalidException();
        }

        tokenCacheService.addToCache(id, UUID.randomUUID().toString());

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
