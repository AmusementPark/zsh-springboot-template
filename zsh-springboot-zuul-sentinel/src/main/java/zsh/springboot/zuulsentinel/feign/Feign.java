package zsh.springboot.zuulsentinel.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="zsh-nacos-producer", fallback = FeignFallback.class)
public interface Feign {
    @GetMapping("/produce") String getProduce();
    @GetMapping("/produce-fb") String getProduceFb();
    @GetMapping("/produce-rl") String getProduceRl();
    @GetMapping("/produce-rln") String getProduceRlN();
}
