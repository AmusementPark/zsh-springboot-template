package zsh.springboot.redisson.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {

    private int timeout;
    private String address;
    private String password;
    private int connectionPoolSize;
    private int connectionMinimumIdleSize;
    private int slaveConnectionPoolSize;
    private int masterConnectionPoolSize;
    private String[] sentinelAddresses;
    private String masterName;
}
