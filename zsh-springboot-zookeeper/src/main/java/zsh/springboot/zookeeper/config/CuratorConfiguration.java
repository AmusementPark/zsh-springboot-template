package zsh.springboot.zookeeper.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.queue.DistributedDelayQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Configuration
public class CuratorConfiguration {

    @Autowired
    CuratorConfigurationProperties curatorConfiguration;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework(RetryPolicy retryPolicy) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
            .connectString(curatorConfiguration.getConnectString())
            .sessionTimeoutMs(curatorConfiguration.getSessionTimeoutMs())
            .connectionTimeoutMs(curatorConfiguration.getConnectionTimeoutMs())
            .retryPolicy(retryPolicy)
        .build();
        return curatorFramework;
    }
    @Bean
    public RetryPolicy retryPolicy() {
        return new RetryNTimes(curatorConfiguration.getRetryCount(), curatorConfiguration.getElapsedTimeMs());
    }

    /**
     * 可重入锁
     * @param curatorFramework
     * @return
     */
    @Bean
    public InterProcessMutex interProcessMutex(CuratorFramework curatorFramework) {
        return new InterProcessMutex(curatorFramework, curatorConfiguration.getDistributedMutexZnode());
    }

    /**
     * Long计数器
     * @param curatorFramework
     * @param retryPolicy
     * @return
     */
    @Bean
    public DistributedAtomicLong distributedAtomicLong(CuratorFramework curatorFramework, RetryPolicy retryPolicy) {
        return new DistributedAtomicLong(curatorFramework, curatorConfiguration.getDistributedAtomicLongZnode(), retryPolicy);
    }
    @Bean
    public DistributedBarrier distributedBarrier(CuratorFramework curatorFramework) {
        return new DistributedBarrier(curatorFramework, curatorConfiguration.getDistributedBarrierZnode());
    }

    /**
     * 读写锁
     * @param curatorFramework
     * @return
     */
    @Bean
    public InterProcessReadWriteLock interProcessReadWriteLock(CuratorFramework curatorFramework) {
        return new InterProcessReadWriteLock(curatorFramework, curatorConfiguration.getDistributedMutexRwZnode());
    }

    /**
     * 延迟队列
     * @param curatorFramework
     * @return
     */
    @Bean
    public DistributedDelayQueue<String> distributedDelayQueued(CuratorFramework curatorFramework) throws Exception {
        QueueConsumer<String> queueConsumer = new QueueConsumer<String>() {
            @Override
            public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                System.out.println("state changed");
            }
            @Override
            public void consumeMessage(String s) {
                System.out.println("消费数据：" + s);
            }
        };
        QueueSerializer<String> queueSerializer = new QueueSerializer<String>() {
            @Override
            public byte[] serialize(String s) {
                try {
                    return s.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            public String deserialize(byte[] bytes) {
                return new String(bytes);
            }
        };
        DistributedDelayQueue distributedDelayQueue = QueueBuilder.builder(
                curatorFramework,
                queueConsumer,
                queueSerializer,
                curatorConfiguration.getDistributedDelayQZnode())
            .buildDelayQueue();

        distributedDelayQueue.start();
        return distributedDelayQueue;
    }
}
