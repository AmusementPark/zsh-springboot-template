package zsh.springboot.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/zk")
public class ZKDistributedSemaphore {

    @Autowired
    private InterProcessSemaphoreV2 interProcessSemaphoreV2;

    @GetMapping("/semaphore")
    public void semaphore() {
        Lease lease = null;
        try {
            lease = interProcessSemaphoreV2.acquire();
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (lease != null) interProcessSemaphoreV2.returnLease(lease);
        }
    }
}
