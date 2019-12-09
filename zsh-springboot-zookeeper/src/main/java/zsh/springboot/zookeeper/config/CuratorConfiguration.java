package zsh.springboot.zookeeper.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CuratorConfiguration {

    @Autowired
    CuratorConfigurationProperties curatorConfiguration;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
        RetryPolicy retryPolicy = new RetryNTimes(curatorConfiguration.getRetryCount(), curatorConfiguration.getElapsedTimeMs());
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
            .connectString(curatorConfiguration.getConnectString())
            .sessionTimeoutMs(curatorConfiguration.getSessionTimeoutMs())
            .connectionTimeoutMs(curatorConfiguration.getConnectionTimeoutMs())
            .retryPolicy(retryPolicy)
        .build();
        return curatorFramework;
    }
}
