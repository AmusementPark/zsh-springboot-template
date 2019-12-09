package zsh.springboot.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ZookeeperApplication {

    @Autowired
    private CuratorFramework curatorFramework;

    public static void main(String[] args) throws Exception {
        ZookeeperApplication app = SpringApplication.run(ZookeeperApplication.class, args).getBean(ZookeeperApplication.class);
//        app.executorService = Executors.newFixedThreadPool(10);
//        app.atomicInteger = new AtomicInteger();
//        app.createNode_PERSISTENT_SEQUENTIAL();
        app.readNodeContent();
    }

//    @Scheduled(fixedDelay=3000)
//    private void ansyc() throws Exception {
////        createNodeWithContent();
//        checkNodeExists();
//    }

//    private void tryCuratorLock() {
//        final Map<String, Long> t = new HashMap<String, Long>() {{
//            put("st", System.currentTimeMillis());
//        }};
//        Runnable r = () -> {
//            while(true) {
//                int calls = atomicInteger.incrementAndGet();
//                try {
//                    interProcessMutex();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (calls % 1000 == 0) {
//                    long et = System.currentTimeMillis();
//                    long st = t.get("st");
//                    System.out.println("call " + calls + " times, consume: "+ (et-st));
//                    t.put("st", et);
//                }
//            }
//        };
//        for (int i=0; i<15; i++) {
//            executorService.submit(r);
//        }
//    }

//    private void tryConcurrentDistributedLock() {
//        Runnable lock = () -> {
////            while (true) {
//                log.info("--------------------------------------------------");
//                    zkDistributedLock.acquireDistributedLock("t");
//            zkDistributedLock.acquireDistributedLock("t");
////                    zkDistributedLock.releaseDistributedLock("t");
////                try {
////                    Thread.sleep(1000);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
//        };
//        Runnable unlock = () -> {
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            zkDistributedLock.releaseDistributedLock("t");
//        };
//        executorService.submit(lock);
//        executorService.submit(unlock);
//    }


//    private void interProcessMutex() throws Exception {
//        InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, "/lock-path");
//        try {
//            interProcessMutex.acquire();
//            // Do nothing
//            System.out.println("TRY-LOCK");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            System.out.println("RELEASE-LOCK");
//            interProcessMutex.release();
//        }
//    }

    public void createNode_PERSISTENT_SEQUENTIAL() throws Exception {
        Stat stat = curatorFramework.checkExists().forPath("/root");
        if (stat == null) {
            curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/root");
        }
//        curatorFramework.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/root/sequential");
        for (int i=0; i<4; i++) {
            curatorFramework.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/root/sequential_");
        }
    }

    /**
     * 创建一个节点，初始内容为空
     * @throws Exception
     */
    public void createNode() throws Exception {
        curatorFramework.create().forPath("path");
    }

    /**
     * 创建一个节点，附带初始化内容
     * @throws Exception
     */
    public void createNodeWithContent() throws Exception {
        Stat stat = curatorFramework.checkExists().forPath("/root");
        if (stat == null) {
            curatorFramework.create().forPath("/root", "DATADATADATA".getBytes());
            System.out.println("CREATE ZOOKEEPER NODE");
        } else {
            byte[] a = curatorFramework.getData().forPath("/root");
            System.out.println(new String(a));
        }
    }

    public void readNodeContent() throws Exception {
        int nodeCount = 0;
        while (true) {
            Thread.sleep(500);
//            Stat stat = curatorFramework.checkExists().forPath("/root/sequential_000000000"+nodeCount);
//            if (stat == null) {
//                System.out.println("EOF");
//                break;
//            } else {
                byte[] a = curatorFramework.getData().forPath("/root");
//                byte[] a = curatorFramework.getData().forPath("/root/sequential_000000000"+nodeCount%8);
                System.out.println("/root/sequential_000000000"+nodeCount%9+" : "+new String(a));
                nodeCount++;
//            }
        }
    }

    /**
     * 创建一个节点，指定创建模式（临时节点），内容为空
     * @throws Exception
     */
    public void createNodeWithMode() throws Exception {
        curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("path");
    }

    /**
     * 检查节点是否存在
     * @throws Exception
     */
    public void checkNodeExists() throws Exception {
        Stat stat = curatorFramework.checkExists().forPath("/path");
        if (stat != null) {
            System.out.println(stat.getDataLength());
        } else {
            System.out.println("-------");
        }
    }

    public void readNode() throws Exception {
        byte[] a = curatorFramework.getData().forPath("/path");
        System.out.println(new String(a));
        try {
            Stat stat = curatorFramework.checkExists().forPath("/pppp");
            if (stat == null) {
                System.out.println("asdfasd");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
