package zsh.springboot.cacheable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CacheableApplicationTests {

    @Autowired
    CacheableService cacheableService;

    @Test
    void contextLoads() {
    }

    @Test
    void l2Cache() {
        String ret = cacheableService.getCaffeineL1AndRedisL2CacheStr("CaffeineL1AndRedisL2Cache");
        System.out.println(ret);
    }

}
