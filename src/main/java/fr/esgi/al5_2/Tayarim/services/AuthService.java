package fr.esgi.al5_2.Tayarim.services;

import fr.esgi.al5_2.Tayarim.auth.JwtHelper;
import fr.esgi.al5_2.Tayarim.auth.TokenCacheService;
import fr.esgi.al5_2.Tayarim.dto.auth.AuthLoginResponseDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.AdministrateurDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.entities.Proprietaire;
import fr.esgi.al5_2.Tayarim.exceptions.*;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final ProprietaireService proprietaireService;
    private final AdministrateurService administrateurService;
    private final JwtHelper jwtHelper;

    private final TokenCacheService tokenCacheService;

    public AuthService(ProprietaireService proprietaireService, AdministrateurService administrateurService, JwtHelper jwtHelper, TokenCacheService tokenCacheService) {
        this.proprietaireService = proprietaireService;
        this.administrateurService = administrateurService;
        this.jwtHelper = jwtHelper;
        this.tokenCacheService = tokenCacheService;
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public AuthLoginResponseDTO login(@NonNull String email, @NonNull String password){
        ProprietaireDTO proprietaireDTO = null;
        AdministrateurDTO administrateurDTO = null;
        Long id;
        boolean isAdmin = false;
        try {
            proprietaireDTO = proprietaireService.getProprietaireByEmail(email);
            id = proprietaireDTO.getId();
        } catch (Exception e){
            try{
                administrateurDTO = administrateurService.getAdministrateurByEmail(email);
                id = administrateurDTO.getId();
                isAdmin = true;
            } catch (Exception exception){
                throw new UtilisateurNotFoundException();
            }

        }

        if( proprietaireDTO != null && !proprietaireService.verifyPassword(password, proprietaireDTO.getId())){
            throw new ProprietaireNotFoundException();
        } else if (administrateurDTO != null && !administrateurService.verifyPassword(password, administrateurDTO.getId())) {
            throw new AdministrateurNotFoundException();
        }

        String uuid = tokenCacheService.getFromCache(id);
        if (uuid == null){
            uuid = UUID.randomUUID().toString();
            tokenCacheService.addToCache(id ,uuid);
        }

        String token = jwtHelper.generateToken(email, uuid, isAdmin);

        return new AuthLoginResponseDTO(
                id,
                token,
                isAdmin
        );
    }

    public AuthLoginResponseDTO auth(@NonNull String token){

        Entry<Long, Boolean> entry = verifyToken(token, false);
        
        return new AuthLoginResponseDTO(
                entry.getKey(),
                token,
                entry.getValue()
        );

    }

    public void logout(@NonNull String token){

        Entry<Long, Boolean> entry = verifyToken(token, false);

        tokenCacheService.addToCache(entry.getKey(), UUID.randomUUID().toString());

    }

    public Entry<Long, Boolean> verifyToken(@NonNull String token, boolean shouldBeAdmin){
        ProprietaireDTO proprietaireDTO;
        AdministrateurDTO administrateurDTO;
        Long id;
        String email;
        boolean isAdmin = jwtHelper.extractAdmin(token);
        if(shouldBeAdmin && !isAdmin){
            throw new TokenExpireOrInvalidException();
        }

        if(isAdmin){
            try{
                administrateurDTO = administrateurService.getAdministrateurByEmail(jwtHelper.extractEmail(token));
                id = administrateurDTO.getId();
                email = administrateurDTO.getEmail();
            } catch (AdministrateurNotFoundException ex){
                throw new TokenExpireOrInvalidException();
            }
        }else{
            try{
                proprietaireDTO = proprietaireService.getProprietaireByEmail(jwtHelper.extractEmail(token));
                id = proprietaireDTO.getId();
                email = proprietaireDTO.getEmail();
            } catch (ProprietaireNotFoundException ex){
                throw new TokenExpireOrInvalidException();
            }
        }

        String uuid = tokenCacheService.getFromCache(id);
        if(uuid == null){
            throw new TokenExpireOrInvalidException();
        }

        if(!jwtHelper.validateToken(token, email, uuid)){
            throw new TokenExpireOrInvalidException();
        }
        return new AbstractMap.SimpleEntry<>(id, isAdmin);
    }
}
