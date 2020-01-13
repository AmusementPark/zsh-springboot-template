package zsh.springboot.aspect.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zsh.springboot.aspect.aop.TimeLoggerAspect;

@Configuration
@EnableConfigurationProperties(AutoProperties.class)
public class ZshToolsStarterAutoConfiguration {

//    @Bean
//    @ConditionalOnMissingBean(TimeConsumeAspect.class)
//    @ConditionalOnBean(KafkaAutoConfiguration.class)
//    public TimeConsumeAspect kafkaMessageAspectBean() {
//        return new TimeConsumeAspect();
//    }

    private final AutoProperties properties;

    public ZshToolsStarterAutoConfiguration(AutoProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(TimeLoggerAspect.class)
    @ConditionalOnClass({
        org.slf4j.Logger.class,
        org.slf4j.LoggerFactory.class
    })
    public TimeLoggerAspect timeConsumeAspectBean() {
        return new TimeLoggerAspect();
    }

}
