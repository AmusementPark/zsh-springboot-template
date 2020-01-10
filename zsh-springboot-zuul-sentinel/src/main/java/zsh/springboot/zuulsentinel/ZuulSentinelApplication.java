package zsh.springboot.zuulsentinel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import zsh.springboot.zuulsentinel.feign.Feign;

//import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@Slf4j
@RestController
@RequestMapping("")
//@EnableZuulProxy
@RefreshScope
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class ZuulSentinelApplication {

    // unsafe
    private int iProduce;
    private int iAnother;
    private int iAnotherV1;
    private int iAnotherV2;

//    @Scheduled(fixedDelay = 1000)
    public void statistics() {
        log.info("----------------------------------------------------");
        log.info("produce   rate: {}", iProduce);
        log.info("another   rate: {}", iAnother);
        log.info("anotherV1 rate: {}", iAnotherV1);
        log.info("anotherV2 rate: {}", iAnotherV2);

        iProduce=0;
        iAnother=0;
        iAnotherV1=0;
        iAnotherV2=0;
    }

    @GetMapping("produce")
    public String produce() {
//        iProduce++;
        return "PRODUCE";
    }

    @GetMapping("another")
    public String another() {
        iAnother++;
        return "ANOTHER";
    }

    @GetMapping("another/v1")
    public String anotherV1() {
        iAnotherV1++;
        return "ANOTHERV1";
    }

    @GetMapping("another/v2")
    public String anotherV2() {
        iAnotherV2++;
        return "ANOTHERV2";
    }

    @GetMapping("timeout")
    public String timeout() {
        try {
            Thread.sleep(1000*15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "TIMEOUT";
    }

    public static void main(String[] args) {
        ZuulSentinelApplication app = SpringApplication.run(ZuulSentinelApplication.class, args).getBean(ZuulSentinelApplication.class);
    }

}
