package zsh.springboot.reactiveweb;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import zsh.springboot.reactiveweb.vo.WrappedLoginVo;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class ReactiveRequestCustomAttributeAssembleFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        WrappedLoginVo wrappedLoginVo = new WrappedLoginVo();
        System.out.println(request.getQueryParams());
        exchange.getAttributes().put("WRAPPED_LOGIN_VO", wrappedLoginVo);
//        wrappedLoginVo.setUsername(request.getQueryParams().get("username"));
//        wrappedLoginVo.setPassword(request.getQueryParams().get("password"));
        return chain.filter(exchange);
    }
}