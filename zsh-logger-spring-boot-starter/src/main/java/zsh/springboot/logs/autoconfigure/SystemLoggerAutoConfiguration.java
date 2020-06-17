package zsh.springboot.logs.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import zsh.springboot.logs.aspect.ZshSystemLoggerAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ZshLoggerProperties.class)
public class SystemLoggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    ZshLoggerProperties zshLoggerProperties() {
        return new ZshLoggerProperties();
    }

    @Bean
    public ZshSystemLoggerAspect zshSystemLoggerAspect() {
        return new ZshSystemLoggerAspect();
    }
}
