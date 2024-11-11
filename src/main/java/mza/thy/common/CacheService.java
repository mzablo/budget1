package mza.thy.common;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CacheService {
    public static final String ACCOUNT_LIST = "accountList";
    private final CacheManager cacheManager;

    public void removeAllCache() {
        cacheManager.getCacheNames().forEach(c -> Objects.requireNonNull(cacheManager.getCache(c)).clear());
    }

    public void removeCache(String cacheName) {
        var cache = cacheManager.getCache(cacheName);
        if (Objects.nonNull(cache)) {
            cache.clear();
        }
    }

}
