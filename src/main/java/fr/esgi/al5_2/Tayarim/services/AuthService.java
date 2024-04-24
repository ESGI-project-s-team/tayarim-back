package fr.esgi.al5_2.Tayarim.services;

import fr.esgi.al5_2.Tayarim.auth.JwtHelper;
import fr.esgi.al5_2.Tayarim.auth.TokenCacheService;
import fr.esgi.al5_2.Tayarim.dto.auth.AuthLoginResponseDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.exceptions.*;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final ProprietaireService proprietaireService;
    private final JwtHelper jwtHelper;

    private final TokenCacheService tokenCacheService;

    public AuthService(ProprietaireService proprietaireService, JwtHelper jwtHelper, TokenCacheService tokenCacheService) {
        this.proprietaireService = proprietaireService;
        this.jwtHelper = jwtHelper;
        this.tokenCacheService = tokenCacheService;
    }

    public AuthLoginResponseDTO loginProprietaire(@NonNull String email, @NonNull String password){
        ProprietaireDTO proprietaireDTO = proprietaireService.getProprietaireByEmail(email);

        if(!proprietaireService.verifyPassword(password, proprietaireDTO.getId())){
            throw new ProprietaireNotFoundException();
        }

        String uuid = tokenCacheService.getFromCache(proprietaireDTO.getId());
        if (uuid == null){
            uuid = UUID.randomUUID().toString();
            tokenCacheService.addToCache(proprietaireDTO.getId() ,uuid);
        }

        String token = jwtHelper.generateToken(email, uuid);

        return new AuthLoginResponseDTO(
                proprietaireDTO.getId(),
                token
        );

    }

    public AuthLoginResponseDTO authProprietaire(@NonNull String token){

        ProprietaireDTO proprietaireDTO = verifyToken(token);
        
        return new AuthLoginResponseDTO(
                proprietaireDTO.getId(),
                token
        );

    }

    public void logoutProprietaire(@NonNull String token){

        ProprietaireDTO proprietaireDTO = verifyToken(token);

        tokenCacheService.addToCache(proprietaireDTO.getId(), UUID.randomUUID().toString());

    }

    public ProprietaireDTO verifyToken(@NonNull String token){
        ProprietaireDTO proprietaireDTO;
        try{
            proprietaireDTO = proprietaireService.getProprietaireByEmail(jwtHelper.extractEmail(token));
        } catch (ProprietaireNotFoundException ex){
            throw new TokenExpireOrInvalidException();
        }

        String uuid = tokenCacheService.getFromCache(proprietaireDTO.getId());
        if(uuid == null){
            throw new TokenExpireOrInvalidException();
        }

        if(!jwtHelper.validateToken(token, proprietaireDTO.getEmail(), uuid)){
            throw new TokenExpireOrInvalidException();
        }
        return proprietaireDTO;
    }
}
