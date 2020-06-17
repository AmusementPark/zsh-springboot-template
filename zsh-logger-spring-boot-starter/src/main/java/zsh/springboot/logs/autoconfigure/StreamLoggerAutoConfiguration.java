package zsh.springboot.logs.autoconfigure;

import zsh.springboot.logs.aspect.ZshStreamLoggerAspect;
import zsh.springboot.logs.async.LinkedBlockQueueExecutor;
import zsh.springboot.logs.stream.ZshStreamLoggerStream;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(ZshStreamLoggerStream.class)
@AutoConfigureAfter(StreamLoggerStreamKafkaAutoConfiguration.class)
public class StreamLoggerAutoConfiguration {
    @Bean
    public ZshStreamLoggerAspect zshStreamLoggerAspect() {
        return new ZshStreamLoggerAspect();
    }
    @Bean
    public LinkedBlockQueueExecutor linkedBlockQueueExecutor() {
        return new LinkedBlockQueueExecutor();
    }
}
