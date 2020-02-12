//package zsh.springboot.reactiveweb;
//
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import reactor.core.publisher.Mono;
//
//public class ReactiveRequestContextHolder {
//
//    static final String CONTEXT_KEY = ServerHttpRequest.class.getName();
//
//    public static Mono<ServerHttpRequest> getRequestMono() {
//        return Mono.subscriberContext().map(ctx -> {
//            System.out.println(ctx);
//            ServerHttpRequest request = ctx.get(CONTEXT_KEY);
//            return request;
//        });
//    }
//
//}
