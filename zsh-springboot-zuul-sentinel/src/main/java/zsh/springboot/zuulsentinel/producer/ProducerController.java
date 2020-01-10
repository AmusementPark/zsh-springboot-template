package zsh.springboot.zuulsentinel.producer;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProducerController {

    @Value("${useLocalCache:abcdefg}")
    private String useLocalCache;

    @GetMapping("config")
    public String config(@RequestParam("c") String param) {
        return useLocalCache+":"+param;
    }

    @GetMapping("produce-fb")
    @SentinelResource(value="sr-produce-fallback", fallback="srProduceFallback")
    public ResponseEntity<String> produceFallback() {
        throw new RuntimeException();
    }

    @GetMapping("produce-rl")
    @SentinelResource(value="sr-produce-ratelimit", blockHandler="srExceptionHandler")
    public ResponseEntity<Vo<String>> produceRatelimit() {
        Vo<String> vo = Vo.<String>builder()
            .code("200")
            .message("OK")
            .data("CREATED")
            .build();
        return ResponseEntity.status(200).body(vo);
    }

    @GetMapping("produce-rln")
    @SentinelResource(value="sr-produce-ratelimit")
    public ResponseEntity<Vo<String>> produceRatelimitN() {
        Vo<String> vo = Vo.<String>builder()
            .code("200")
            .message("OK")
            .data("CREATED")
            .build();
        return ResponseEntity.status(200).body(vo);
    }

    // Fallback 函数，函数签名与原函数一致.
    public ResponseEntity<String> srProduceFallback() {
        log.info("-------------------------------------------------------1");
        return ResponseEntity.status(500).body("SENTINEL-FALLBACK");
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public ResponseEntity<String> srExceptionHandler(BlockException ex) {
        log.info("-------------------------------------------------------2");
        return ResponseEntity.status(500).body("SENTINEL-RATELIMIT");
    }
}
