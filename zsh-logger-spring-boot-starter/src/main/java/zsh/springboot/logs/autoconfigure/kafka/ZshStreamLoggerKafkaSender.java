package zsh.springboot.logs.autoconfigure.kafka;

import com.alibaba.fastjson.JSON;
import zsh.springboot.logs.model.ZshStreamLoggerModel;
import zsh.springboot.logs.stream.ZshStreamLoggerStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author Shenghao.Zhu
 * Kafka 日志缓冲器
 */
@Slf4j
@Component
public class ZshStreamLoggerKafkaSender implements ZshStreamLoggerStream {

    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.concurrency:1}")
    private int concurrency;

    Random random = new Random();

    private void sendMessage(String topic, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(
                topic,
                random.nextInt(concurrency),
                System.currentTimeMillis(),
                null,
                value);
        kafkaTemplate.send(record);
    }

    private void sendMessage(String topic, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(
                topic,
                random.nextInt(concurrency),
                System.currentTimeMillis(),
                key,
                value);
        kafkaTemplate.send(record);
    }

//    public Flux<SenderResult<String>> reactiveSendMessage(String topic, String value) {
//        return kafkaSender.send(Mono.just(value).map(d -> {
//            ProducerRecord<String, String> record = new ProducerRecord<>(topic,
//                    random.nextInt(concurrency), System.currentTimeMillis(), topic, d);
//            return SenderRecord.create(record, null);
//        }));
//    }

    @Override
    public void sendRosWebLog(ZshStreamLoggerModel model) {
        sendMessage("ros-logs-topic", JSON.toJSONString(model));
    }

    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
}

