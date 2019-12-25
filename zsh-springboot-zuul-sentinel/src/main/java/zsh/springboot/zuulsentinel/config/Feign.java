package zsh.springboot.zuulsentinel.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="zsh-nacos-producer")
public interface Feign {
    @GetMapping("/produce") String getProduce();
}
