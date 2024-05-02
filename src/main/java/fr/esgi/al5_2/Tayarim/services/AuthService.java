package fr.esgi.al5_2.Tayarim.services;

import fr.esgi.al5_2.Tayarim.auth.JwtHelper;
import fr.esgi.al5_2.Tayarim.auth.TokenCacheService;
import fr.esgi.al5_2.Tayarim.dto.auth.AuthLoginResponseDTO;
import fr.esgi.al5_2.Tayarim.dto.auth.AuthResponseDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.AdministrateurDTO;
import fr.esgi.al5_2.Tayarim.dto.proprietaire.ProprietaireDTO;
import fr.esgi.al5_2.Tayarim.exceptions.AdministrateurNotFoundException;
import fr.esgi.al5_2.Tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5_2.Tayarim.exceptions.TokenExpireOrInvalidException;
import fr.esgi.al5_2.Tayarim.exceptions.UtilisateurNotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * Classe de service gérant l'authentification des utilisateurs.
 */
@Service
@Transactional(readOnly = true)
public class AuthService {

  private final ProprietaireService proprietaireService;
  private final AdministrateurService administrateurService;
  private final JwtHelper jwtHelper;

  private final TokenCacheService tokenCacheService;

  /**
   * Constructeur pour le service d'authentification.
   *
   * @param proprietaireService   Le service de gestion des propriétaires.
   * @param administrateurService Le service de gestion des administrateurs.
   * @param jwtHelper             L'outil d'assistance JWT.
   * @param tokenCacheService     Le service de cache des tokens.
   */
  public AuthService(ProprietaireService proprietaireService,
      AdministrateurService administrateurService, JwtHelper jwtHelper,
      TokenCacheService tokenCacheService) {
    this.proprietaireService = proprietaireService;
    this.administrateurService = administrateurService;
    this.jwtHelper = jwtHelper;
    this.tokenCacheService = tokenCacheService;
  }

  /**
   * Tente de connecter un utilisateur avec son email et mot de passe.
   *
   * @param email    L'email de l'utilisateur.
   * @param password Le mot de passe de l'utilisateur.
   * @return {@link AuthLoginResponseDTO}
   * @throws UtilisateurNotFoundException    Si aucun utilisateur n'est trouvé avec cet email.
   * @throws ProprietaireNotFoundException   Si le propriétaire est trouvé mais le mot de passe est
   *                                         incorrect.
   * @throws AdministrateurNotFoundException Si l'administrateur est trouvé mais le mot de passe est
   *                                         incorrect.
   */
  @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
  public AuthLoginResponseDTO login(@NonNull String email, @NonNull String password) {
    ProprietaireDTO proprietaireDTO = null;
    AdministrateurDTO administrateurDTO = null;
    Long id;
    boolean isAdmin = false;
    String nom;
    String prenom;
    String numTel;
    try {
      proprietaireDTO = proprietaireService.getProprietaireByEmail(email);
      id = proprietaireDTO.getId();
      nom = proprietaireDTO.getNom();
      prenom = proprietaireDTO.getPrenom();
      numTel = proprietaireDTO.getNumTel();
    } catch (Exception e) {
      try {
        administrateurDTO = administrateurService.getAdministrateurByEmail(email);
        id = administrateurDTO.getId();
        nom = administrateurDTO.getNom();
        prenom = administrateurDTO.getPrenom();
        numTel = administrateurDTO.getNumTel();
        isAdmin = true;
      } catch (Exception exception) {
        throw new UtilisateurNotFoundException();
      }

    }

    if (proprietaireDTO != null && !proprietaireService.verifyPassword(password,
        proprietaireDTO.getId())) {
      throw new ProprietaireNotFoundException();
    } else if (administrateurDTO != null && !administrateurService.verifyPassword(password,
        administrateurDTO.getId())) {
      throw new AdministrateurNotFoundException();
    }

    String uuid = tokenCacheService.getFromCache(id);
    if (uuid == null) {
      uuid = UUID.randomUUID().toString();
      tokenCacheService.addToCache(id, uuid);
    }

    String token = jwtHelper.generateToken(email, uuid, isAdmin);

    return new AuthLoginResponseDTO(id, token, isAdmin, nom, prenom, email, numTel);
  }

  /**
   * Authentifie un utilisateur à partir d'un token.
   *
   * @param token Le token JWT fourni à l'utilisateur après une connexion réussie.
   * @return {@link AuthResponseDTO}
   * @throws TokenExpireOrInvalidException Si le token est expiré ou invalide.
   */
  public AuthResponseDTO auth(@NonNull String token) {

    Entry<Long, Boolean> entry = verifyToken(token, false);

    return new AuthResponseDTO(entry.getKey(), token, entry.getValue());

  }

  /**
   * Déconnecte un utilisateur en invalidant le token actuel et en générant un nouveau.
   *
   * @param token Le token JWT de l'utilisateur à déconnecter.
   */
  public void logout(@NonNull String token) {

    Entry<Long, Boolean> entry = verifyToken(token, false);

    tokenCacheService.addToCache(entry.getKey(), UUID.randomUUID().toString());

  }

  /**
   * Vérifie la validité du token JWT, en vérifiant également si l'utilisateur devrait être un
   * administrateur.
   *
   * @param token         Le token JWT à vérifier.
   * @param shouldBeAdmin Booléen indiquant si l'utilisateur doit être vérifié en tant
   *                      qu'administrateur.
   * @return {@link Entry}
   * @throws TokenExpireOrInvalidException Si le token est invalide ou expiré, ou si l'utilisateur
   *                                       n'est pas autorisé comme administrateur lorsque requis.
   */
  public Entry<Long, Boolean> verifyToken(@NonNull String token, boolean shouldBeAdmin) {
    ProprietaireDTO proprietaireDTO;
    AdministrateurDTO administrateurDTO;
    Long id;
    String email;
    boolean isAdmin = jwtHelper.extractAdmin(token);
    if (shouldBeAdmin && !isAdmin) {
      throw new TokenExpireOrInvalidException();
    }

    if (isAdmin) {
      try {
        administrateurDTO = administrateurService.getAdministrateurByEmail(
            jwtHelper.extractEmail(token));
        id = administrateurDTO.getId();
        email = administrateurDTO.getEmail();
      } catch (AdministrateurNotFoundException ex) {
        throw new TokenExpireOrInvalidException();
      }
    } else {
      try {
        proprietaireDTO = proprietaireService.getProprietaireByEmail(jwtHelper.extractEmail(token));
        id = proprietaireDTO.getId();
        email = proprietaireDTO.getEmail();
      } catch (ProprietaireNotFoundException ex) {
        throw new TokenExpireOrInvalidException();
      }
    }

    String uuid = tokenCacheService.getFromCache(id);
    if (uuid == null) {
      throw new TokenExpireOrInvalidException();
    }

    if (!jwtHelper.validateToken(token, email, uuid)) {
      throw new TokenExpireOrInvalidException();
    }
    return new AbstractMap.SimpleEntry<>(id, isAdmin);
  }
}
