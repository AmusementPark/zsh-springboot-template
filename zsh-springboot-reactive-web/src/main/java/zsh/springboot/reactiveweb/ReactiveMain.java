package zsh.springboot.reactiveweb;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ReactiveMain {

    public static void main(String[] args) throws InterruptedException {
        test1();
    }

    public static void test0() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux.range(1, 2)
            .map( i -> {
                System.out.println("MAP AGAIN");
                return i;
            })
            .collectList()
            .flatMapMany(integers -> {
                return Mono.just(integers);
            })
            .subscribeOn(Schedulers.parallel())
            //使用Schedulers.parallel()线程池执行之后的操作
            .doOnComplete(() -> countDownLatch.countDown())

            .subscribe(i -> {
                System.out.println("Current Thread is "
                        + Thread.currentThread().getName() + ", value " + i);
            });
        //如果使用了Scheduler，则subscribe是异步的，主线程必须阻塞才行
        System.out.println(Thread.currentThread().getName() + "-Main thread blocking");
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "-Flow complete,Main thread run and finished");
    }

    public static void test1() {
        List<String> origin = Arrays.asList("AAA", "BBB", "CCC", "DDD", "EEE");
        Flux.fromIterable(origin)
            .filter(str -> {
                return !str.startsWith("E");
            })
            .map(str -> {
                return str.replace("B","A");
            })
            .collectMap(str -> "KEY:"+str)
            .doOnNext(map -> {
                System.out.println(map);
            })
            .subscribe();
        System.out.println("BLOCKING FINISH");
    }

}
