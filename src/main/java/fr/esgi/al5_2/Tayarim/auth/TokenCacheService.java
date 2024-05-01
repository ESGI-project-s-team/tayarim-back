package fr.esgi.al5_2.Tayarim.auth;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service pour gérer le cache des tokens utilisateurs.
 */
@Service
public class TokenCacheService {

  private final Map<Long, String> cache = new ConcurrentHashMap<>();

  /**
   * Ajoute ou met à jour un token dans le cache avec l'identifiant spécifié.
   *
   * @param key   L'identifiant de l'utilisateur.
   * @param value Le token à stocker dans le cache.
   */
  public void addToCache(Long key, String value) {
    cache.put(key, value);
  }

  /**
   * Récupère le token associé à un identifiant utilisateur du cache.
   *
   * @param key L'identifiant de l'utilisateur.
   * @return Le token associé, ou null si aucun token n'est trouvé.
   */
  public String getFromCache(Long key) {
    return cache.get(key);
  }

  /**
   * Retourne la totalité du cache sous forme d'une carte.
   *
   * @return La carte contenant tous les couples identifiant-token.
   */
  public Map<Long, String> getFullCache() {
    return cache;
  }

}