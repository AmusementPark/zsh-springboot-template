package zsh.springboot.cacheable.l2;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouchunchen
 * 混合缓存配置
 * L1 - Caffeine
 * L2 - Redis
 */
@Configuration
@ConditionalOnProperty(value = "cache.type", havingValue = "L2")
public class CaffeineL1AndRedisL2CacheConfiguration {

    public static final int DEFAULT_MAXSIZE = 10000;
    public static final int DEFAULT_TTL = 600;
    /**
     * 定义cache名称、超时时长（秒）、最大容量
     * 每个cache缺省：10秒超时、最多缓存50000条数据，需要修改可以在构造方法的参数中指定。
     */
    public enum Caches {
        CA(60,100),          //有效期600秒
        CB(7200,1000),  //有效期2个小时 , 最大容量1000
        CaffeineL1AndRedisL2Cache(60,100)
        ;
        Caches() {
        }
        Caches(int ttl) {
            this.ttl = ttl;
        }
        Caches(int ttl, int maxSize) {
            this.ttl = ttl;
            this.maxSize = maxSize;
        }
        private int maxSize=DEFAULT_MAXSIZE;	//最大數量
        private int ttl=DEFAULT_TTL;		//过期时间（秒）
        public int getMaxSize() {
            return maxSize;
        }
        public int getTtl() {
            return ttl;
        }
    }

//    @Bean("caffeineCacheCA")
//    public CaffeineCache caffeineCacheCA() {
//        return new CaffeineCache(Caches.CA.name(),
//                Caffeine.newBuilder().recordStats()
//                        .initialCapacity(100)
//                        .expireAfterWrite(Caches.CA.getTtl(), TimeUnit.SECONDS)
//                        .maximumSize(Caches.CA.getMaxSize())
//                        .build());
//    }
//
//    @Bean("caffeineCacheCB")
//    public CaffeineCache caffeineCacheCB() {
//        return new CaffeineCache(Caches.CB.name(),
//                Caffeine.newBuilder().recordStats()
//                        .initialCapacity(100)
//                        .expireAfterWrite(Caches.CB.getTtl(), TimeUnit.SECONDS)
//                        .maximumSize(Caches.CB.getMaxSize())
//                        .build());
//    }

    /**
     * 首要配置
     * @param caffeineCacheManager
     * @param redisCacheManager
     * @return
     */
    @Bean
    @Primary
    public CacheManager caffeineL2AndRedisL2CacheManager(
            @Autowired @Qualifier("caffeineCacheManager") CacheManager caffeineCacheManager,
            @Autowired @Qualifier("redisCacheManager") CacheManager redisCacheManager) {
        CaffeineL1AndRedisL2CacheManager caffeineL1AndRedisL2CacheManager = new CaffeineL1AndRedisL2CacheManager();
        caffeineL1AndRedisL2CacheManager.setCaches(Collections.singletonList(
            new CaffeineL1AndRedisL2Cache(
                "CaffeineL1AndRedisL2Cache",
                (SimpleCacheManager)caffeineCacheManager,
                (RedisCacheManager)redisCacheManager)
        ));
        return caffeineL1AndRedisL2CacheManager;
    }

    /**
     * 创建基于Caffeine的Cache Manager,等价于
     * spring.cache.cache-names=foo,bar
     * spring.cache.caffeine.spec=initialCapacity=10,maximumSize=500,expireAfterAccess=600s
     * @return
     */
    @Bean("caffeineCacheManager")
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caches = new ArrayList<>();
        for(Caches c : Caches.values()) {
            caches.add(new CaffeineCache(c.name(),
                    Caffeine.newBuilder().recordStats()
                            .initialCapacity(100)
                            .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                            .maximumSize(c.getMaxSize())
                            .build())
            );
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(@Autowired RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer()); // GenericJackson2JsonRedisSerializer
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer()); // GenericJackson2JsonRedisSerializer
        template.setEnableTransactionSupport(true);
        return template;
    }

    @Bean(name="redisCacheManager")
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(1000))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();
        RedisCacheManager.RedisCacheManagerBuilder builder =
                RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory);
        return builder.transactionAware().cacheDefaults(config).build();
    }
}
