package zsh.springboot.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("")
@SpringBootApplication
public class KafkaApplication {

    public static void main(String[] args) {
        KafkaApplication kafkaApplication = SpringApplication.run(KafkaApplication.class, args).getBean(KafkaApplication.class);
//        kafkaApplication.producer();
    }

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("produce")
    public void producer() {
        ProducerRecord<String, String> record = new ProducerRecord<>(
            "T1",
//            RandomUtils.nextInt(4),
                0,
            System.currentTimeMillis(),
            RandomStringUtils.randomAlphabetic(3),
            RandomStringUtils.randomAlphabetic(3)
        );
        try {
            SendResult<String, String> sendResult = kafkaTemplate.send(record).get();
            sendResult.getProducerRecord();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Batch 消费模式
     * @param consumerRecordList
     * @param ack
     */
//    @KafkaListener(id="defaultConsumerGroup-1"
//        , topics = "T1"
//        , errorHandler = "KafkaListenerErrorHandlerImpl"
//    )
//    public void batchConsumer(List<ConsumerRecord<String, String>> consumerRecordList, Acknowledgment ack) {
////        consumerRecordList.forEach(consumerRecord -> {
////            log.info(Thread.currentThread().getId() + "-" + consumerRecord.key() + ":" + consumerRecord.value());
////            ack.acknowledge();
////        });
//        log.info(Thread.currentThread().getId() + "- THIS BATCH SIZE: [{}]", consumerRecordList.size());
////        for (int i=0; i< consumerRecordList.size(); i++) {
////            if (consumerRecordList.size() > 1 && i == 0) {
////                throw new RuntimeException("Hey you!");
////            }
////            log.info(Thread.currentThread().getId() + "-" + consumerRecordList.get(i).key() + ":" + consumerRecordList.get(i).value());
////            ack.acknowledge();
////        }
//        ack.acknowledge();
//    }


    @KafkaListener(id="defaultConsumerGroup-1", topics = "T1", errorHandler = "KafkaListenerErrorHandlerImpl")
    public void testKafkaConsumerGrp1P1(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) {
        try {
            if (RandomUtils.nextBoolean()) {
                log.info("CONSUME SUCC - GRP1");
                log.info("{}", consumerRecord);
            } else {
                log.info("CONSUME ERRO - GRP1");
                throw new RuntimeException("Hey You!");
            }
        } finally {
            ack.acknowledge();  // 仍然ACK，但是已经进入重试流程
        }
    }

    @KafkaListener(id="defaultConsumerGroup-2", topics = "T1", errorHandler = "KafkaListenerErrorHandlerImpl")
    public void testKafkaConsumerGrp2(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) {
        try {
            if (RandomUtils.nextBoolean()) {
                log.info("CONSUME SUCC - GRP2");
                log.info("{}", consumerRecord);
            } else {
                log.info("CONSUME ERRO - GRP2");
                throw new RuntimeException("Hey You!");
            }
        } finally {
            ack.acknowledge();  // 仍然ACK，但是已经进入重试流程
        }
    }


    /**
     * 不设置异常处理Handler，重试
     * @param consumerRecord
     */
//    @KafkaListener(id="defaultConsumerGroup-1", topics = "T1")
//    public void testKafkaConsumer(ConsumerRecord<String, String> consumerRecord) {
//        log.info("{}", consumerRecord);
//    }

    // 进入死信，需要手工去消费死信队列，死信队列命名：XXX.DLT
//    @KafkaListener(id = "dltGroup", topics = "T1.DLT")
//    public void dltListen(String input, Acknowledgment ack) {
//        log.info("Received from DLT: " + input);
//        ack.acknowledge();
//    }
}
