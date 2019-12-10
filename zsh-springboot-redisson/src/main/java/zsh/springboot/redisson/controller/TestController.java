package zsh.springboot.redisson.controller;

import com.eelve.redissionlock.distributedlock.DistributedLocker;
import com.eelve.redissionlock.service.testAnnoService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @Description Created by zeng.yubo on 2019/8/11.
 */
//@RestController
public class TestController {

//    @Autowired
//    TestLockDAO dao;

    @Autowired
    DistributedLocker distributedLocker;

    @Autowired
    testAnnoService testAnnoService;

//    @RequestMapping("/test")
    public String test(){

        Runnable r = () -> {
            while (true) {
                try {
                    Thread.sleep(RandomUtils.nextInt(1000, 2000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                testAnnoService.testAnno();
            }
        };

        new Thread(r).start();
        new Thread(r).start();

        return "OK";


//        distributedLocker.tryLock("LK:foo",10,60, ()->{
//                TestLockVo vo = dao.getVoById(1);
//                int count = vo.getCountnum();
//                if (count == 0) {
//                    System.out.println("===================错误");
//                    return ;
//                } else {
//                    --count;
//                    System.out.println("yes");
//                }
//                vo.setCountnum(count);
//                dao.updateVoById(vo);
//        });
//        RLock lock = distributedLocker.lock("LK:foo",10);
//        lock.lock();
//        try {
//
//        }catch (Exception e){
//
//        }finally {
//            lock.unlock();
//        }
    }
}
