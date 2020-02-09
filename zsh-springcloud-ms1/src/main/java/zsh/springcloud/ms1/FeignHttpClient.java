package zsh.springcloud.ms1;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "MS2-CLINET", url = "http://localhost:9099")
public interface FeignHttpClient {
    @GetMapping("api/get")
    String getAlohaFromMicroService2();
}
