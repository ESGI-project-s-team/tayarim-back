package fr.esgi.al5.tayarim.services;


import fr.esgi.al5.tayarim.auth.JwtHelper;
import fr.esgi.al5.tayarim.auth.TokenCacheService;
import fr.esgi.al5.tayarim.dto.auth.AuthLoginResponseDto;
import fr.esgi.al5.tayarim.dto.auth.AuthResponseDto;
import fr.esgi.al5.tayarim.dto.proprietaire.AdministrateurDto;
import fr.esgi.al5.tayarim.dto.proprietaire.ProprietaireDto;
import fr.esgi.al5.tayarim.entities.Utilisateur;
import fr.esgi.al5.tayarim.exceptions.AdministrateurNotFoundException;
import fr.esgi.al5.tayarim.exceptions.ProprietaireNotFoundException;
import fr.esgi.al5.tayarim.exceptions.TokenExpireOrInvalidException;
import fr.esgi.al5.tayarim.exceptions.UtilisateurNotFoundException;
import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.UUID;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


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
   * @return {@link AuthLoginResponseDto}
   * @throws UtilisateurNotFoundException    Si aucun utilisateur n'est trouvé avec cet email.
   * @throws ProprietaireNotFoundException   Si le propriétaire est trouvé mais le mot de passe est
   *                                         incorrect.
   * @throws AdministrateurNotFoundException Si l'administrateur est trouvé mais le mot de passe est
   *                                         incorrect.
   */
  @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
  public AuthLoginResponseDto login(@NonNull String email, @NonNull String password) {
    ProprietaireDto proprietaireDto = null;
    AdministrateurDto administrateurDto = null;
    Long id;
    boolean isAdmin = false;
    String nom;
    String prenom;
    String numTel;
    boolean isPasswordUpdated;
    try {
      proprietaireDto = proprietaireService.getProprietaireByEmail(email);
      id = proprietaireDto.getId();
      nom = proprietaireDto.getNom();
      prenom = proprietaireDto.getPrenom();
      numTel = proprietaireDto.getNumTel();
      isPasswordUpdated = proprietaireDto.getIsPasswordUpdated();
    } catch (Exception e) {
      try {
        isPasswordUpdated = true;
        administrateurDto = administrateurService.getAdministrateurByEmail(email);
        id = administrateurDto.getId();
        nom = administrateurDto.getNom();
        prenom = administrateurDto.getPrenom();
        numTel = administrateurDto.getNumTel();
        isAdmin = true;
      } catch (Exception exception) {
        throw new UtilisateurNotFoundException();
      }

    }

    if (proprietaireDto != null && !proprietaireService.verifyPassword(password,
        proprietaireDto.getId())) {
      throw new ProprietaireNotFoundException();
    } else if (administrateurDto != null && !administrateurService.verifyPassword(password,
        administrateurDto.getId())) {
      throw new AdministrateurNotFoundException();
    }

    String uuid = tokenCacheService.getFromCache(id);
    if (uuid == null) {
      uuid = UUID.randomUUID().toString();
      tokenCacheService.addToCache(id, uuid);
    }

    String token = jwtHelper.generateToken(id, uuid, isAdmin);

    return new AuthLoginResponseDto(id, token, false, nom, prenom, email, numTel,
        isPasswordUpdated);
  }

  /**
   * Authentifie un utilisateur à partir d'un token.
   *
   * @param token Le token JWT fourni à l'utilisateur après une connexion réussie.
   * @return {@link AuthResponseDto}
   * @throws TokenExpireOrInvalidException Si le token est expiré ou invalide.
   */
  public AuthResponseDto auth(@NonNull String token) {

    Entry<Long, Boolean> entry = verifyToken(token, false);

    Boolean isPasswordUpdated = true;

    if (!entry.getValue()) {
      isPasswordUpdated = proprietaireService.getProprietaireById(entry.getKey(), false)
          .getIsPasswordUpdated();
    }

    return new AuthResponseDto(entry.getKey(), token, entry.getValue(), isPasswordUpdated);

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
    boolean isAdmin = jwtHelper.extractAdmin(token);
    if (shouldBeAdmin && !isAdmin) {
      throw new TokenExpireOrInvalidException();
    }
    Long id;
    AdministrateurDto administrateurDto;
    ProprietaireDto proprietaireDto;
    if (isAdmin) {
      try {
        administrateurDto = administrateurService.getAdministrateurById(
            jwtHelper.extractId(token));
        /* if the email has been updated and that the user want to re-do an update without re-login,
         * the token will not be valid anymore
         */
        id = administrateurDto.getId();
      } catch (AdministrateurNotFoundException ex) {
        throw new TokenExpireOrInvalidException();
      }
    } else {
      try {
        proprietaireDto = proprietaireService.getProprietaireById(jwtHelper.extractId(token),
            false);
        /* if the email has been updated and that the user want to re-do an update without re-login,
         * the token will not be valid anymore
         */
        id = proprietaireDto.getId();
      } catch (ProprietaireNotFoundException ex) {
        throw new TokenExpireOrInvalidException();
      }
    }

    String uuid = tokenCacheService.getFromCache(id);
    if (uuid == null) {
      throw new TokenExpireOrInvalidException();
    }

    if (!jwtHelper.validateToken(token, id, uuid)) {
      throw new TokenExpireOrInvalidException();
    }
    return new AbstractMap.SimpleEntry<>(id, isAdmin);
  }
}
