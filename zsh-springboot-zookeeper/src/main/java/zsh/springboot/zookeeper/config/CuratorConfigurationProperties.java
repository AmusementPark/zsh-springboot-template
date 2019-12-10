package zsh.springboot.zookeeper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "curator")
public class CuratorConfigurationProperties {
    private int retryCount;
    private int elapsedTimeMs;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
    private String connectString;
    private String distributedAtomicLongZnode;
    private String distributedBarrierZnode;
    private String distributedMutexZnode;
    private String distributedMutexRwZnode;
    private String distributedDelayQZnode;
    private String distributedSemaphoreZnode;
    private Integer distributedSemaphoreLease;
}
