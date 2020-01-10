package zsh.springboot.zuulsentinel.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import zsh.springboot.zuulsentinel.feign.Feign;

@RestController
public class ConsumerController {

    @Autowired
    private Feign feign;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("feign")
    public String feignCall() {
        return feign.getProduceRlN();
    }

    @GetMapping("fallback")
    public String fallback() {
        return feign.getProduceFb();
    }

    @GetMapping("ratelimit")
    public String rateLimit() {
        return feign.getProduceRl();
    }

    @GetMapping("consume")
    public String consume() {
        return restTemplate.getForObject("http://zsh-nacos-producer/produce-rl", String.class);
    }

    @GetMapping("consume-fb")
    public String consumefb() {
        return restTemplate.getForObject("http://zsh-nacos-producer/produce-fb", String.class);
    }
}
