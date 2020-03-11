package zsh.springboot.reactiveweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import zsh.springboot.reactiveweb.vo.LoginVo;
import zsh.springboot.reactiveweb.vo.WrappedLoginVo;

@RestController
@SpringBootApplication
@MapperScan(value = "zsh.springboot.reactiveweb.ds0.dao*")
public class ReactiveWebApplication {

    public static void main(String[] args) {
        ReactiveWebApplication application = SpringApplication.run(ReactiveWebApplication.class, args).getBean(ReactiveWebApplication.class);
//        application.alohaController().block();
    }

    @PutMapping("api")
    @UserActionLogger(module = "zsh-springboot-aspect", userSessionFetchAdapter=AuthorizationFetchAdapter.class)
    public String controller(@RequestBody LoginVo loginVo, @RequestAttribute("WRAPPED_LOGIN_VO") WrappedLoginVo wrappedLoginVo, ServerWebExchange exchange) {
        System.out.println(wrappedLoginVo);
        return "FINISH";
    }

//    @PutMapping("api")
//    @UserActionLogger(module = "zsh-springboot-aspect")
//    public Mono<String> controller(@RequestBody LoginVo loginVo, LoginVo ano, ServerWebExchange serverWebExchange) {
//        return Mono.fromCallable(() -> {
//            Thread.sleep(1000);
//            return "STRING";
//        })
//        .publishOn(Schedulers.elastic())
//        .map(str -> {
//            return str+"@THREAD"+Thread.currentThread().getId();
//        })
//        .publishOn(Schedulers.elastic())
//        .flatMap(str -> {
//            return Mono.just(str+"@FINISH");
//        });
//    }
//
//    @UserActionLogger
//    public Mono<String> alohaController() {
//        return Mono.fromCallable(() -> {
//            Thread.sleep(1000);
//            return "STRING";
//        })
//        .publishOn(Schedulers.elastic())
//        .map(str -> {
//            return str+"@THREAD"+Thread.currentThread().getId();
//        })
//        .publishOn(Schedulers.elastic())
//        .flatMap(str -> {
//            return Mono.just(str+"@FINISH");
//        });
//    }
}
