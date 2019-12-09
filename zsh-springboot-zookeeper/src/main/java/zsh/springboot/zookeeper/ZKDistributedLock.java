package zsh.springboot.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
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
public class ZKDistributedLock {

    @Autowired
    private InterProcessMutex interProcessMutex;
    @Autowired
    private InterProcessReadWriteLock interProcessReadWriteLock;

    /**
     * 简单互斥锁，可重入
     * ab -c 100 -n 1000 http://localhost:7089/api/zk/mutex
     * -----------------------------------------------------
     * This is ApacheBench, Version 2.3 <$Revision: 1826891 $>
     * Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
     * Licensed to The Apache Software Foundation, http://www.apache.org/
     *
     * Benchmarking localhost (be patient)
     * Completed 100 requests
     * Completed 200 requests
     * Completed 300 requests
     * Completed 400 requests
     * Completed 500 requests
     * Completed 600 requests
     * Completed 700 requests
     * Completed 800 requests
     * Completed 900 requests
     * Completed 1000 requests
     * Finished 1000 requests
     *
     *
     * Server Software:
     * Server Hostname:        localhost
     * Server Port:            7089
     *
     * Document Path:          /api/zk/mutex
     * Document Length:        0 bytes
     *
     * Concurrency Level:      100
     * Time taken for tests:   251.981 seconds
     * Complete requests:      1000
     * Failed requests:        0
     * Total transferred:      94000 bytes
     * HTML transferred:       0 bytes
     * Requests per second:    3.97 [#/sec] (mean)
     * Time per request:       25198.069 [ms] (mean)
     * Time per request:       251.981 [ms] (mean, across all concurrent requests)
     * Transfer rate:          0.36 [Kbytes/sec] received
     *
     * Connection Times (ms)
     *               min  mean[+/-sd] median   max
     * Connect:        0    1   2.1      0      11
     * Processing:   101 24212 4728.2  25431   28054
     * Waiting:      101 24212 4728.3  25431   28054
     * Total:        111 24213 4726.5  25432   28055
     *
     * Percentage of the requests served within a certain time (ms)
     *   50%  25432
     *   66%  25838
     *   75%  26119
     *   80%  26284
     *   90%  26656
     *   95%  26998
     *   98%  27438
     *   99%  27586
     *  100%  28055 (longest request)
     *
     * @throws Exception
     */
    @GetMapping("mutex")
    public void interProcessMutex() throws Exception {
        try {
            interProcessMutex.acquire();
//            interProcessMutex.acquire(1000, TimeUnit.MILLISECONDS);
            // Do nothing
            log.info("TRY-LOCK");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        finally {
            log.info("RELEASE-LOCK");
            interProcessMutex.release();
        }
    }

    /**
     * 共享读锁，性能非常好
     *
     * ab -c 100 -n 1000 http://localhost:7089/api/zk/mutex-r
     * -----------------------------------------------------
     * This is ApacheBench, Version 2.3 <$Revision: 1826891 $>
     * Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
     * Licensed to The Apache Software Foundation, http://www.apache.org/
     *
     * Benchmarking localhost (be patient)
     * Completed 100 requests
     * Completed 200 requests
     * Completed 300 requests
     * Completed 400 requests
     * Completed 500 requests
     * Completed 600 requests
     * Completed 700 requests
     * Completed 800 requests
     * Completed 900 requests
     * Completed 1000 requests
     * Finished 1000 requests
     *
     *
     * Server Software:
     * Server Hostname:        localhost
     * Server Port:            7089
     *
     * Document Path:          /api/zk/lock-rw
     * Document Length:        0 bytes
     *
     * Concurrency Level:      100
     * Time taken for tests:   13.051 seconds
     * Complete requests:      1000
     * Failed requests:        0
     * Total transferred:      94000 bytes
     * HTML transferred:       0 bytes
     * Requests per second:    76.62 [#/sec] (mean)
     * Time per request:       1305.099 [ms] (mean)
     * Time per request:       13.051 [ms] (mean, across all concurrent requests)
     * Transfer rate:          7.03 [Kbytes/sec] received
     *
     * Connection Times (ms)
     *               min  mean[+/-sd] median   max
     * Connect:        0    2   3.1      1      16
     * Processing:   178 1238 273.7   1298    1801
     * Waiting:      178 1236 273.7   1297    1795
     * Total:        189 1240 272.1   1300    1801
     *
     * Percentage of the requests served within a certain time (ms)
     *   50%   1300
     *   66%   1361
     *   75%   1411
     *   80%   1427
     *   90%   1523
     *   95%   1600
     *   98%   1677
     *   99%   1706
     *  100%   1801 (longest request)
     *
     * @throws Exception
     */
    @GetMapping("mutex-r")
    public void interProcessRwLock() throws Exception {
        try {
            interProcessReadWriteLock.readLock().acquire();
            Thread.sleep(10);
            log.info("TRY-LOCK");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        finally {
            log.info("RELEASE-LOCK");
            interProcessReadWriteLock.readLock().release();
        }
    }

}
