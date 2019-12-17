package zsh.springboot.zuulsentinel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("")
//@EnableZuulProxy
@RefreshScope
//@EnableDiscoveryClient
@SpringBootApplication
public class ZuulSentinelApplication {

//    @NacosInjected
//    private NamingService namingService;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Value("${useLocalCache:false}")
//    private boolean useLocalCache;
//
//    @Autowired
//    private RestTemplate restTemplate;

//    @GetMapping("aaa")
//    public String get() {
//        return String.valueOf(useLocalCache);
//    }

//    @GetMapping("get")
//    public String get() {
//        return restTemplate.getForObject("http://zsh-zuul-sentinel-2/get2/", String.class);
//    }


    public static void main(String[] args) throws InterruptedException {
        ApplicationContext app = SpringApplication.run(ZuulSentinelApplication.class, args);

        while (true) {
            //当动态配置刷新时，会更新到 Enviroment中，因此这里每隔一秒中从Enviroment中获取配置
            String useLocalCache = app.getEnvironment().getProperty("useLocalCache");
            log.info(useLocalCache);
            TimeUnit.SECONDS.sleep(4);
        }
    }

}
