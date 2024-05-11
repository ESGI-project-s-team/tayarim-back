package fr.esgi.al5.tayarim.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer le cache des tokens utilisateurs.
 */
@Service
public class TokenCacheService {

  private final Map<Long, String> cache = new ConcurrentHashMap<>();

  /**
   * Ajoute ou met à jour un Uuid dans le cache avec l'identifiant spécifié.
   *
   * @param key   L'identifiant de l'utilisateur.
   * @param value L'Uuid à stocker dans le cache.
   */
  public void addToCache(Long key, String value) {
    cache.put(key, value);
  }

  /**
   * Récupère l'Uuid associé à un identifiant utilisateur du cache.
   *
   * @param key L'identifiant de l'utilisateur.
   * @return L'Uuid' associé, ou null si aucun Uuid n'est trouvé.
   */
  public String getFromCache(Long key) {
    return cache.get(key);
  }

  /**
   * Retourne la totalité du cache sous forme d'une carte.
   *
   * @return La carte contenant tous les couples identifiant-Uuid.
   */
  public Map<Long, String> getFullCache() {
    return cache;
  }

}