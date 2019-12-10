package zsh.springboot.redisson;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class RedissonApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedissonApplication.class, args);
    }

    @Autowired
    private RedissonClient redissonClient;

    public void rlock() {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        AtomicInteger atomicInteger = new AtomicInteger();
        RLock rLock = redissonClient.getFairLock("222");
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong("INCRLONG");
        RSet<String> rSet = redissonClient.getSet("SETSTR");
        Runnable r = () -> {
            while(true) {
//                int ret = atomicInteger.get();
//                if (ret >= 2000) {
//                    System.out.println("CountDown");
//                    countDownLatch.countDown();
//                    break;
//                }
//                boolean succ = atomicInteger.compareAndSet(ret, ret+1);
//                if (!succ) {
//                    continue;
//                }
                System.out.println(Thread.currentThread().getId()+" - FETCH LOCK BEGIN");
                rLock.lock(30, TimeUnit.SECONDS);
                System.out.println(Thread.currentThread().getId()+" - FETCH LOCK");
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rLock.unlock();
//                System.out.println(Thread.currentThread().getId()+" - RETURN LOCK END");
            }
        };
        new Thread(r).start();
        new Thread(r).start();
//        new Thread(r).start();
//        new Thread(r).start();
//        long s = System.currentTimeMillis();
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        long e = System.currentTimeMillis();
//        System.out.println(e-s+ ":"+atomicInteger.get());
    }


    public void unitTestSet() {
        RSortedSet<String> rSet = redissonClient.getSortedSet("unitTestSet");
        rSet.clear();
        for (int i=0; i<200; i++) {
            rSet.add(RandomStringUtils.randomAlphabetic(10));
        }
//        Set<String> result = rSet.readAll();
//        result.forEach(str -> System.out.println(str));
        RFuture<Collection<String>> future = rSet.readAllAsync();
        future.handleAsync((result, exception) -> {
            System.out.println(result);
            return result;
        });
    }
}
