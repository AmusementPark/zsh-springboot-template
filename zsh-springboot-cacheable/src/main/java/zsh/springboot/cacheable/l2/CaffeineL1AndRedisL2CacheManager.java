package zsh.springboot.cacheable.l2;

import org.springframework.cache.support.SimpleCacheManager;

/**
 * 一个自定义的二级缓存
 * L1->CAFFEINE
 * L2->REDIS
 */
public class CaffeineL1AndRedisL2CacheManager extends SimpleCacheManager {

}
