package zsh.springboot.zuulsentinel.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.zuul.fallback.BlockResponse;
import com.alibaba.csp.sentinel.adapter.gateway.zuul.fallback.ZuulBlockFallbackProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 对于超过QPS的流量，进入此方法熔断
 */
@Slf4j
@Component
public class SentinelFallBackHandler implements ZuulBlockFallbackProvider {
    @Override
    public String getRoute() {
        return null; //api服务id，如果需要所有调用都支持回退，则return "*"或return null
    }

    @Override
    public BlockResponse fallbackResponse(String s, Throwable throwable) {
        log.info("SENTINEL FALLBACK");
        return new BlockResponse(500, "FALLS", null);
    }
}
