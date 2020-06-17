package zsh.springboot.logs.async;

import zsh.springboot.logs.model.ZshStreamLoggerModel;
import zsh.springboot.logs.stream.ZshStreamLoggerStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 为了防止切面记录日志流因为异常而阻塞，采用队列+异步的方式记录日志流，队列满则抛弃
 * @author Shenghao.Zhu
 */
@Slf4j
@Component
public class LinkedBlockQueueExecutor implements InitializingBean {

    @Autowired
    private ZshStreamLoggerStream rosWebLogStream;
    private LinkedBlockingQueue<ZshStreamLoggerModel> linkedBlockingQueue = new LinkedBlockingQueue<>(1000);

    @Override
    public void afterPropertiesSet() throws Exception {
        Runnable logStreamRunnable = () -> {
            while (true) {
                ZshStreamLoggerModel rosLogsRecordModel;
                try {
                    rosLogsRecordModel = linkedBlockingQueue.poll(60*30, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    log.info("[ROS-LOG-SPRINGBOOT-STARTER] STOP STREAMING ROS-WEB-LOG");
                    break;
                }
                if (rosLogsRecordModel != null) {
                    rosWebLogStream.sendRosWebLog(rosLogsRecordModel);
                } else {
                    log.debug("[ROS-LOG-SPRINGBOOT-STARTER] FETCH NOTHING TO STREAM");
                }
            }
        };
        new Thread(logStreamRunnable).start();
    }

    /**
     * 接受任务
     * @param rosLogsRecordModel
     */
    public void offerRosLogsRecordModel(ZshStreamLoggerModel rosLogsRecordModel) {
        this.linkedBlockingQueue.offer(rosLogsRecordModel);
    }
}
