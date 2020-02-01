package zsh.springboot.cacheable;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@EnableCaching
@RequestMapping
@RestController
@SpringBootApplication
public class CacheableApplication {

    @Autowired
    private CacheableService cacheableService;
//    @Autowired
//    @Qualifier("caffeineCacheCA")
//    private CaffeineCache caffeineCacheCA;
//    @Autowired
//    @Qualifier("caffeineCacheCB")
//    private CaffeineCache caffeineCacheCB;


    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(CacheableApplication.class, args);
        CacheableApplication cacheableApplication = applicationContext.getBean(CacheableApplication.class);

//        String str = cacheableApplication.cacheableService.getCaffeineStr("AAA");
//        log.info(str);
//        for (int i =0;i < 200; i++) {
//            cacheableApplication.caffeine(RandomStringUtils.randomAlphabetic(3));
//        }
    }

    @GetMapping("re")
    public String redis() {
        return cacheableService.getRedisStr("AAA");
    }

    @GetMapping("ca")
    public String caffeine(@RequestParam(value="cache",required = false, defaultValue = "AAA") String cache) {
//        log.info("{}", caffeineCacheCA.getNativeCache().estimatedSize());
        return cacheableService.getCaffeineStr(cache);
    }

    @GetMapping("car")
    public String caffeineRefresh(@RequestParam(value="cache",required = false, defaultValue = "AAA") String cache) {
        return cacheableService.getAndPutCaffeineStr(cache);
    }

    @GetMapping("cad")
    public String caffeineEvict(@RequestParam(value="cache",required = false, defaultValue = "AAA") String cache) {
        return cacheableService.getAndEvictCaffeineStr(cache);
    }

    @GetMapping("cax")
    public String caffeineCondition(@RequestParam(value="cache",required = false, defaultValue = "AAA") String cache) {
        return cacheableService.getCaffeineStrCondition(cache);
    }

    @GetMapping("l2")
    public String l2Cache(@RequestParam(value="cache",required = false, defaultValue = "AAA") String cache) {
        String ret = cacheableService.getCaffeineL1AndRedisL2CacheStr(cache);
        log.info("{}", ret);
        return ret;
    }
}
