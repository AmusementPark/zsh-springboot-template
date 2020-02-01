package zsh.springboot.cacheable;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheableService {

    /**
     * @param hi
     * @return
     */
    @Cacheable(cacheNames="XA", key="#hi")
    public String getRedisStr(String hi) {
        log.info("No Cache, Caching...");
        return "Aloha, 大猪小猪在菜盘！";
    }

    @Cacheable(cacheNames="CaffeineL1AndRedisL2Cache", key="#str")
    public String getCaffeineL1AndRedisL2CacheStr(String str) {
        log.info("No Cache, Caching..., {}", str);
        return "Aloha, 大猪小猪在菜盘！"+str;
    }

    /**
     * @param str
     * @return
     */
    @Cacheable(cacheNames="CA", key="#str")
    public String getCaffeineStr(String str) {
        log.info("No Cache, Caching..., {}", str);
        return "Aloha, 大猪小猪在菜盘！"+str;
    }

    @CachePut(cacheNames="CA", key="#str")
    public String getAndPutCaffeineStr(String str) {
        log.info("@CachePut..., {}", str);
        return "Aloha, 大猪小猪在菜盘！"+str;
    }

    @CacheEvict(cacheNames="CA", key="#str")
    public String getAndEvictCaffeineStr(String str) {
        log.info("No Cache, Caching..., {}", str);
        return "Aloha, 大猪小猪在菜盘！"+str;
    }

    // 以User中的id值为key，且 condition 条件满足则缓存
    @Cacheable(cacheNames="CB", key="#str", condition="#str.equals('AAA')")
    public String getCaffeineStrCondition(String str) {
        log.info("No Cache, Caching... NO AAA {}", str);
        return "Aloha, 大猪小猪在菜盘！"+str;
    }
}
