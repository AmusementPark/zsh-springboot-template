package zsh.springboot.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.queue.DistributedDelayQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分布式锁
 */
@Slf4j
@RestController
@RequestMapping("api/zk")
public class ZKDistributedDelayQ {

    @Autowired
    private DistributedDelayQueue<String> distributedDelayQueue;

    @GetMapping("delay-q")
    public void delayQueue() throws Exception {
        distributedDelayQueue.put("ALOHA", 1000);
    }

}
