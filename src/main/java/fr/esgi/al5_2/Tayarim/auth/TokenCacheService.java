package fr.esgi.al5_2.Tayarim.auth;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenCacheService {
    private final Map<Long, String> cache = new ConcurrentHashMap<>();

    public void addToCache(Long key, String value) {
        cache.put(key, value);
    }

    public String getFromCache(Long key) {
        return cache.get(key);
    }

    public Map<Long, String> getFullCache() {
        return cache;
    }

}