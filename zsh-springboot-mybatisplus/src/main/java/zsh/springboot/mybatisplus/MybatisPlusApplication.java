package zsh.springboot.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import zsh.springboot.mybatisplus.ds0.model.ShareHolder;
import zsh.springboot.mybatisplus.ds0.service.ShareHolderService;
import zsh.springboot.mybatisplus.ds1.model.Share;
import zsh.springboot.mybatisplus.ds1.service.ShareService;
import zsh.springboot.mybatisplus.joinds.model.*;
import zsh.springboot.mybatisplus.joinds.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class MybatisPlusApplication {

    @Autowired(required = false)
    private ShareHolderService shareHolderService;
    @Autowired(required = false)
    private ShareService shareService;

    public static void main(String[] args) {

        MybatisPlusApplication app = SpringApplication.run(MybatisPlusApplication.class, args).getBean(MybatisPlusApplication.class);
        /**
         * INSERT
         */
//        app.shareService.save(Share.builder().shareCode("600030").shareName("中信证券").build());
//        app.shareService.save(Share.builder().shareCode("600000").shareName("浦发银行").build());
//        app.shareHolderService.save(ShareHolder.builder().username("88021927").password("99990099").build());
        /**
         * UPDATE
         */
//        UpdateWrapper<Share> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.set("share_code", "000002").set("share_name", "万科企业").eq("share_code", "000002");
//        app.shareService.update(updateWrapper);
        /**
         * TRANSACTIONAL
         */
//        app.shareService.insertTransactionalAndRollBack();
        app.joinDsInit();
    }



    @Autowired(required = false)
    private JoinMainService joinMainService;
    @Autowired(required = false)
    private JoinT1Service joinT1Service;
    @Autowired(required = false)
    private JoinT2Service joinT2Service;
    @Autowired(required = false)
    private JoinT3Service joinT3Service;
    @Autowired(required = false)
    private JoinT4Service joinT4Service;

    private final static int BATCH_TIME=500;
    private final static int BATCH_SIZE=500;
    private final static int THREAD_NUMS=8;

    /**
     * 灌入数据，进行JOIN性能测试
     */
    public void joinDsInit() {
//        joinDsInitJoinMain();
//        joinDsInitJoinTX();
    }

    private void joinDsInitJoinMain() {
        CountDownLatch countDownLatch = new CountDownLatch(4);
        Runnable r = () -> {
            for (int i = 0; i < BATCH_TIME; i++) {
                List<JoinMain> list = new ArrayList<>();
                for (int j = 0; j < BATCH_SIZE; j++) {
                    JoinMain joinMain = new JoinMain();
                    joinMain.setJoinT1Id(RandomUtils.nextLong(10000000, 30000000));
                    joinMain.setJoinT2Id(RandomUtils.nextLong(10000000, 30000000));
                    joinMain.setJoinT3Id(RandomUtils.nextLong(10000000, 30000000));
                    joinMain.setJoinT4Id(RandomUtils.nextLong(10000000, 30000000));
                    joinMain.setJoinMainName(RandomStringUtils.randomAlphabetic(20));
                    joinMain.setJoinMainDesc(RandomStringUtils.randomAlphabetic(20));
                    list.add(joinMain);
                }
                System.out.println(Thread.currentThread().getId()+ " : " +i);
                joinMainService.saveBatch(list);
            }
            countDownLatch.countDown();
        };
        for (int i = 0; i< THREAD_NUMS; i++) {
            new Thread(r).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void joinDsInitJoinTX() {
        QueryWrapper<JoinMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderBy(true, false, "join_t1_id").last("LIMIT "+BATCH_TIME);
        List<JoinMain> list = joinMainService.list(queryWrapper);

        CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUMS);
        Runnable r = () -> {
            for (int i = 0; i < list.size(); i++) {
                List<JoinT1> insert1 = new ArrayList<>();
                for (int j = 0; j < BATCH_SIZE; j++) {
                    JoinT1 joinT1 = new JoinT1();
                    joinT1.setJoinT1Id(list.get(i).getJoinT1Id());
                    joinT1.setJoinT1Name(RandomStringUtils.randomAlphabetic(20));
                    joinT1.setJoinT1Desc(RandomStringUtils.randomAlphabetic(20));
                    insert1.add(joinT1);
                }
                joinT1Service.saveBatch(insert1);

                List<JoinT2> insert2 = new ArrayList<>();
                for (int j = 0; j < BATCH_SIZE; j++) {
                    JoinT2 joinT2 = new JoinT2();
                    joinT2.setJoinT2Id(list.get(i).getJoinT2Id());
                    joinT2.setJoinT2Name(RandomStringUtils.randomAlphabetic(20));
                    joinT2.setJoinT2Desc(RandomStringUtils.randomAlphabetic(20));
                    insert2.add(joinT2);
                }
                joinT2Service.saveBatch(insert2);

                List<JoinT3> insert3 = new ArrayList<>();
                for (int j = 0; j < BATCH_SIZE; j++) {
                    JoinT3 joinT3 = new JoinT3();
                    joinT3.setJoinT3Id(list.get(i).getJoinT3Id());
                    joinT3.setJoinT3Name(RandomStringUtils.randomAlphabetic(20));
                    joinT3.setJoinT3Desc(RandomStringUtils.randomAlphabetic(20));
                    insert3.add(joinT3);
                }
                joinT3Service.saveBatch(insert3);

                List<JoinT4> insert4 = new ArrayList<>();
                for (int j = 0; j < BATCH_SIZE; j++) {
                    JoinT4 joinT4 = new JoinT4();
                    joinT4.setJoinT4Id(list.get(i).getJoinT4Id());
                    joinT4.setJoinT4Name(RandomStringUtils.randomAlphabetic(20));
                    joinT4.setJoinT4Desc(RandomStringUtils.randomAlphabetic(20));
                    insert4.add(joinT4);
                }
                joinT4Service.saveBatch(insert4);

                System.out.println(Thread.currentThread().getId()+ " : " +i);
            }
            countDownLatch.countDown();
        };
        for (int i = 0; i< THREAD_NUMS ; i++) {
            new Thread(r).start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
