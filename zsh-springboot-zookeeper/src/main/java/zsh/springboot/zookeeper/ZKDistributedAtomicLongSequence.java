package zsh.springboot.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分布式计数器，用来产生全局递增唯一ID
 * 本机测试ab,远程zookeeper机器1C2G单节点
 */
@Slf4j
@RestController
@RequestMapping("api/zk")
public class ZKDistributedAtomicLongSequence {

    @Autowired
    private DistributedAtomicLong distributedAtomicLong;

    /**
     * ab -c 100 -n 1000 http://localhost:7089/api/zk/incr
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
     * Document Path:          /api/zk/incr
     * Document Length:        5 bytes
     *
     * Concurrency Level:      100
     * Time taken for tests:   37.615 seconds
     * Complete requests:      1000
     * Failed requests:        0
     * Total transferred:      112000 bytes
     * HTML transferred:       5000 bytes
     * Requests per second:    26.59 [#/sec] (mean)
     * Time per request:       3761.519 [ms] (mean)
     * Time per request:       37.615 [ms] (mean, across all concurrent requests)
     * Transfer rate:          2.91 [Kbytes/sec] received
     *
     * Connection Times (ms)
     *               min  mean[+/-sd] median   max
     * Connect:        0    1   1.4      0       7
     * Processing:    50 3543 2032.3   3093   19548
     * Waiting:       50 3543 2032.3   3093   19547
     * Total:         57 3544 2032.0   3093   19552
     *
     * Percentage of the requests served within a certain time (ms)
     *   50%   3093
     *   66%   3726
     *   75%   4433
     *   80%   4747
     *   90%   5964
     *   95%   7276
     *   98%   9449
     *   99%  11207
     *  100%  19552 (longest request)
     * @return
     */
    @GetMapping("/incr")
    public Long sequence() {
        try {
            while (true) {
                AtomicValue<Long> sequence = distributedAtomicLong.increment();
                if (sequence.succeeded()) {
                    Long ret = sequence.postValue();
                    log.info("{}", ret);
                    return ret;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return -1L;
        }
    }

    /**
     * ab -c 100 -n 1000 http://localhost:7089/api/zk/incr-2
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
     * Document Path:          /api/zk/incr-2
     * Document Length:        4 bytes
     *
     * Concurrency Level:      100
     * Time taken for tests:   93.544 seconds
     * Complete requests:      1000
     * Failed requests:        0
     * Total transferred:      111000 bytes
     * HTML transferred:       4000 bytes
     * Requests per second:    10.69 [#/sec] (mean)
     * Time per request:       9354.420 [ms] (mean)
     * Time per request:       93.544 [ms] (mean, across all concurrent requests)
     * Transfer rate:          1.16 [Kbytes/sec] received
     *
     * Connection Times (ms)
     *               min  mean[+/-sd] median   max
     * Connect:        0    1   2.7      0      14
     * Processing:   114 8912 4277.9   7344   37393
     * Waiting:      114 8912 4277.9   7344   37393
     * Total:        127 8914 4277.4   7344   37393
     *
     * Percentage of the requests served within a certain time (ms)
     *   50%   7344
     *   66%   8818
     *   75%  10657
     *   80%  11743
     *   90%  14778
     *   95%  17607
     *   98%  21740
     *   99%  24109
     *  100%  37393 (longest request)
     * @return
     */
    @GetMapping("/incr-2")
    public Long sequence2() {
        try {
            while (true) {
                AtomicValue<Long> previous = distributedAtomicLong.get();
                AtomicValue<Long> sequence = distributedAtomicLong.compareAndSet(previous.postValue(), previous.postValue()+1);
                if (sequence.succeeded()) {
                    Long ret = sequence.postValue();
                    log.info("{}", ret);
                    return ret;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return -1L;
        }
    }

    /**
    public void sequence() {
        final CountDownLatch cd = new CountDownLatch(1);
        final CyclicBarrier cb = new CyclicBarrier(11);

        Runnable runnable = () -> {
            try {
                //这里使用CountDownLatch，是为了保证10个线程同时启动，每个县被创建的线程都在await，等10个创建完成后，在主线程调用了countDown
                cd.await();
                log.warn("TREADID={}, START", Thread.currentThread().getId());
                AtomicValue<Long> sequence = distributedAtomicLong.increment();
                if (sequence.succeeded()) {
                    Long seq = sequence.postValue();
                    log.info("threadId={}, sequence={}", Thread.currentThread().getId(), seq);
                } else {
                    log.warn("threadId={}, no sequence", Thread.currentThread().getId());
                }
                cb.await();
            } catch (Exception e) {
                log.error("acquire section exception.", e);
            }
        };

        for(int i = 0; i < 10; i++){
            new Thread(runnable,"t" + i).start();
        }
        try {
            Thread.sleep(300);
            //10个线程开始执行
            cd.countDown();
            log.info("countDown");
            cb.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        log.info("End");
    }
    */
}
