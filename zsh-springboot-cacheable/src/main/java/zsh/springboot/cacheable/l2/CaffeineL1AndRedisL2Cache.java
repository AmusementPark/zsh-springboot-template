package zsh.springboot.cacheable.l2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.concurrent.Callable;

/**
 * 一个自定义的二级缓存
 * L1->CAFFEINE
 * L2->REDIS
 */
@Slf4j
public class CaffeineL1AndRedisL2Cache implements Cache {

    private String cacheName;
    private RedisCacheManager redisCacheManager;
    private SimpleCacheManager caffeineCacheManager;

    /**
     * we define not allowed null value on all cache
     * @param cacheName
     * @param caffeineCacheManager
     * @param redisCacheManager
     */
    public CaffeineL1AndRedisL2Cache(String cacheName,
                                     SimpleCacheManager caffeineCacheManager,
                                     RedisCacheManager redisCacheManager) {
        this.redisCacheManager = redisCacheManager;
        this.caffeineCacheManager = caffeineCacheManager;
        this.cacheName = cacheName;
    }

    @Override
    public String getName() {
        return this.cacheName;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    /**
     * 涉及到的情况：
     * 1. L1未过期，L2过期
     * 2. L1过期，L2未过期
     * 3. L1过期，L2过期
     * 4. L1未过期，L2未过期
     * @param key
     * @return
     */
    @Override
    public ValueWrapper get(Object key) {
        Cache redisCache = this.redisCacheManager.getCache(this.cacheName);
        Cache caffeineCache = this.caffeineCacheManager.getCache(this.cacheName);

        Cache.ValueWrapper caffeineWrapper = caffeineCache.get(key);
        // L1过期
        if (caffeineWrapper == null) {
            Cache.ValueWrapper redisWrapper = redisCache.get(key);
            if (redisWrapper == null) {
                return null;
            }
            // 重新设置L1
            caffeineCache.putIfAbsent(key, redisWrapper.get());
            return caffeineCache.get(key);
        }
        else {
            return caffeineCache.get(key);
        }
//        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return null;
    }

    /**
     * 处理上, 往redis, caffeine上都写入
     * @param key
     * @param value
     */
    @Override
    public void put(Object key, Object value) {
        // 首先写入redis
        Cache redisCache = this.redisCacheManager.getCache(this.cacheName);
        // 确保销毁老的存在的cache
        redisCache.evictIfPresent(key);
        redisCache.putIfAbsent(key, value);
        log.info("put cache redis.");
        // 再次写入caffeine
        Cache caffeineCache = this.caffeineCacheManager.getCache(this.cacheName);
        // 确保销毁老的存在的cache
        caffeineCache.evictIfPresent(key);
        caffeineCache.putIfAbsent(key, value);
        log.info("put cache caffeine.");
    }

    /**
     *
     * @param key
     */
    @Override
    public void evict(Object key) {
        caffeineCacheManager.getCache(this.cacheName).evict(key);
        redisCacheManager.getCache(this.cacheName).evict(key);
    }

    /**
     *
     */
    @Override
    public void clear() {
        caffeineCacheManager.getCache(this.cacheName).clear();
        redisCacheManager.getCache(this.cacheName).clear();
    }

}
