package zsh.springboot.logs.autoconfigure;

import zsh.springboot.logs.aspect.ZshSystemLoggerAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SystemLoggerAutoConfiguration {
    @Bean
    public ZshSystemLoggerAspect zshSystemLoggerAspect() {
        return new ZshSystemLoggerAspect();
    }
}
