package zsh.springboot.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@Slf4j
@SpringBootApplication
public class ZookeeperApplication implements InitializingBean {

    @Autowired
    private CuratorFramework curatorFramework;
    private PathChildrenCache pathChildrenCache;

    @Override
    public void afterPropertiesSet() throws Exception {
        pathChildrenCache = new PathChildrenCache(curatorFramework, "/insert_node_with_mode_sequential", true);
        pathChildrenCache.getListenable().addListener((CuratorFramework curatorFramework, PathChildrenCacheEvent event) -> {
//            log.info("update event type:" + event.getType() + ",path:" + event.getData().getPath() + ",data:" + new String(event.getData().getData()));
//            log.info("update event type:" + event.getType() + ",path:" + event.getData().getPath());
            log.info(" ------------ update event type:" + event.getType());
//            List<ChildData> childDataList = pathChildrenCache.getCurrentData();
//            if (childDataList != null && childDataList.size() > 0) {
//                System.out.println("path all children list:");
//                for (ChildData childData : childDataList) {
//                    System.out.println("path:" + childData.getPath() + "," + new String(childData.getData()));
//                }
//            }
        });
        pathChildrenCache.start();
    }

    public static void main(String[] args) throws Exception {
        ZookeeperApplication app = SpringApplication.run(ZookeeperApplication.class, args).getBean(ZookeeperApplication.class);
//        app.insertNodeWithModeEmpty();
        Thread.sleep(10000);
        app.curatorFramework.setData().forPath("/insert_node_with_mode_sequential", "2222".getBytes());
        Thread.sleep(10000);
        app.curatorFramework.delete().forPath("/insert_node_with_mode_sequential");
    }

    /**
     * 清空所有节点
     * @throws Exception
     */
    public void truncateAllNode() throws Exception {
        curatorFramework.delete().forPath("/insert_node_empty");
        curatorFramework.delete().forPath("/insert_node_with_content");
        curatorFramework.delete().forPath("/insert_node_with_mode_empty");
        // 花式删除方式
//        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/insert_node_with_mode_sequential");
//        curatorFramework.delete().withVersion(9999).forPath("insert_node_empty");
//        curatorFramework.delete().guaranteed().forPath("insert_node_empty");
    }

    /**
     * 创建一个节点，初始内容为空
     * @throws Exception
     */
    public void insertNodeEmpty() throws Exception {
        curatorFramework.create().forPath("insert_node_empty");
    }
    /**
     * 创建一个节点，指定创建模式（临时节点），内容为空
     * 创建一个持久顺序节点
     * @throws Exception
     */
    public void insertNodeWithModeEmpty() throws Exception {
//        curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath("insert_node_with_mode_empty");
        String sss = curatorFramework.create().forPath("/insert_node_with_mode_sequential");
        System.out.println(sss);
        Thread.sleep(2000);
        curatorFramework.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/insert_node_with_mode_sequential/sequential_");
    }

    /**
     * 创建一个节点，附带初始化内容
     * @throws Exception
     */
    public void insertNodeWithContent() throws Exception {
        Stat stat = curatorFramework.checkExists().forPath("/insert_node_with_content");
        if (stat == null) {
            curatorFramework.create().forPath("/insert_node_with_content", "INSERT NODE WITH CONTENT".getBytes());
        } else {
            byte[] a = curatorFramework.getData().forPath("/insert_node_with_content");
            log.info("{}", new String(a));
        }
    }

    /**
     * 检查节点是否存在
     * @throws Exception
     */
    public Stat checkNodeExists() throws Exception {
        Stat stat = curatorFramework.checkExists().forPath("/path");
        if (stat != null) {
            System.out.println(stat.getDataLength());
        }
        return stat;
    }


}
