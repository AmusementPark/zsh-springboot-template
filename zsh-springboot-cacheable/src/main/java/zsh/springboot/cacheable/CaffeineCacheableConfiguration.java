package zsh.springboot.cacheable;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouchunchen
 * 咖啡因缓存配置
 */

@Configuration
@ConditionalOnProperty(value = "cache.type", havingValue = "caffeine")
public class CaffeineCacheableConfiguration {

    public static final int DEFAULT_MAXSIZE = 10000;
    public static final int DEFAULT_TTL = 600;
    /**
     * 定义cache名称、超时时长（秒）、最大容量
     * 每个cache缺省：10秒超时、最多缓存50000条数据，需要修改可以在构造方法的参数中指定。
     */
    public enum Caches {
        CA(60,100),          //有效期600秒
        CB(7200,1000),  //有效期2个小时 , 最大容量1000
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

    @Bean("caffeineCacheCA")
    public CaffeineCache caffeineCacheCA() {
        return new CaffeineCache(Caches.CA.name(),
                Caffeine.newBuilder().recordStats()
                        .initialCapacity(100)
                        .expireAfterWrite(Caches.CA.getTtl(), TimeUnit.SECONDS)
                        .maximumSize(Caches.CA.getMaxSize())
                        .build());
    }

    @Bean("caffeineCacheCB")
    public CaffeineCache caffeineCacheCB() {
        return new CaffeineCache(Caches.CB.name(),
                Caffeine.newBuilder().recordStats()
                        .initialCapacity(100)
                        .expireAfterWrite(Caches.CB.getTtl(), TimeUnit.SECONDS)
                        .maximumSize(Caches.CB.getMaxSize())
                        .build());
    }

    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caches = new ArrayList<>();
        caches.add(caffeineCacheCA());
        caches.add(caffeineCacheCB());
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    /**
     * 创建基于Caffeine的Cache Manager,等价于
     * spring.cache.cache-names=foo,bar
     * spring.cache.caffeine.spec=initialCapacity=10,maximumSize=500,expireAfterAccess=600s
     * @return
     */
//    @Bean
//    public CacheManager caffeineCacheManager() {
//        SimpleCacheManager cacheManager = new SimpleCacheManager();
//        ArrayList<CaffeineCache> caches = new ArrayList<>();
//        for(Caches c : Caches.values()) {
//            caches.add(new CaffeineCache(c.name(),
//                    Caffeine.newBuilder().recordStats()
//                            .initialCapacity(100)
//                            .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
//                            .maximumSize(c.getMaxSize())
//                            .build())
//            );
//        }
//        cacheManager.setCaches(caches);
//        return cacheManager;
//    }

}
