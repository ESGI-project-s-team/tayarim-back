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
import org.springframework.transaction.annotation.Transactional;

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

    public AuthLoginResponseDTO login(@NonNull String email, @NonNull String password){
        System.out.println("1");
        ProprietaireDTO proprietaireDTO = null;
        AdministrateurDTO administrateurDTO = null;
        Long id;
        boolean isAdmin = false;
        try {
            System.out.println("2.1");
            proprietaireDTO = proprietaireService.getProprietaireByEmail(email);
            id = proprietaireDTO.getId();
        } catch (Exception e){
            System.out.println("2.2");
            try{
                administrateurDTO = administrateurService.getAdministrateurByEmail(email);
                id = administrateurDTO.getId();
                isAdmin = true;
            } catch (Exception exception){
                throw new UtilisateurNotFoundException();
            }

        }
        System.out.println("3");

        if( proprietaireDTO != null && !proprietaireService.verifyPassword(password, proprietaireDTO.getId())){
            System.out.println("4.1");
            throw new ProprietaireNotFoundException();
        } else if (administrateurDTO != null && !administrateurService.verifyPassword(password, administrateurDTO.getId())) {
            System.out.println("4.2");
            throw new AdministrateurNotFoundException();
        }
        System.out.println("5");

        String uuid = tokenCacheService.getFromCache(id);
        if (uuid == null){
            uuid = UUID.randomUUID().toString();
            tokenCacheService.addToCache(id ,uuid);
        }
        System.out.println("6");

        String token = jwtHelper.generateToken(email, uuid, isAdmin);
        System.out.println("7");

        return new AuthLoginResponseDTO(
                id,
                token
        );

    }

    public AuthLoginResponseDTO auth(@NonNull String token){

        Long id = verifyToken(token, false);
        
        return new AuthLoginResponseDTO(
                id,
                token
        );

    }

    public void logout(@NonNull String token){

        Long id = verifyToken(token,false);

        tokenCacheService.addToCache(id, UUID.randomUUID().toString());

    }

    public Long verifyToken(@NonNull String token, boolean shouldBeAdmin){
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
        return id;
    }
}
