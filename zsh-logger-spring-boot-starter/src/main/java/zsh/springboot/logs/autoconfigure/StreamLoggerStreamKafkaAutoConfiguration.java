package zsh.springboot.logs.autoconfigure;

import zsh.springboot.logs.autoconfigure.kafka.ZshStreamLoggerKafkaSender;
import zsh.springboot.logs.stream.ZshStreamLoggerStream;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Shenghao.Zhu
 * 配置网络日志
 */
@Configuration
@ConditionalOnMissingBean(ZshStreamLoggerStream.class)
@ConditionalOnClass(KafkaTemplate.class)
@AutoConfigureAfter(KafkaAutoConfiguration.class)
public class StreamLoggerStreamKafkaAutoConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.producer.retries}")
    private String retries;
//    @Value("${spring.kafka.producer.batch-size}")
//    private String batchSize;
    @Value("${spring.kafka.producer.buffer-memory}")
    private String bufferMemory;
    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;
    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        return props;
    }

    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public ZshStreamLoggerStream zshStreamLoggerStream() {
        ZshStreamLoggerKafkaSender rosWebLogKafkaSender = new ZshStreamLoggerKafkaSender();
        rosWebLogKafkaSender.setKafkaTemplate(new KafkaTemplate<>(producerFactory()));
        return rosWebLogKafkaSender;
    }
}
