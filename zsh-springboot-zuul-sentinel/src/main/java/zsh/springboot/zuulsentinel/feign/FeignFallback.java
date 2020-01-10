package zsh.springboot.zuulsentinel.feign;

import org.springframework.stereotype.Component;

@Component
public class FeignFallback implements Feign {
    @Override
    public String getProduce() {
        return "PRODUCE FALLBACK";
    }

    @Override
    public String getProduceFb() {
        return "COMES FROM FEIGN FALLBACK";
    }

    @Override
    public String getProduceRl() {
        return "COMES FROM FEIGN RATELIMIT";
    }

    @Override
    public String getProduceRlN() {
        System.out.println("getProduceRlN");
        return "getProduceRlN";
    }
}
