package zsh.springboot.kafka.manual;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

/**
 * 手动创建的KAFKA-CONSUMER
 */
@Component
public class KafkaCustomConsumer {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;
    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;

    public Consumer<String, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "dltGroup");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);

        // Create the consumer using props.
        final Consumer<String, String> consumer = new KafkaConsumer<>(props);
        // Subscribe to the topic.
        consumer.subscribe(Collections.singletonList("T1.DLT"), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                System.out.println("onPartitionsRevoked");
            }
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                System.out.println("onPartitionsAssigned");
            }
        });
        return consumer;
    }

    public void runConsumer() throws InterruptedException {
        final Consumer<String, String> consumer = createConsumer();

        final int giveUp = 10;
        int noRecordsCount = 0;

        while (true) {
            final ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);

            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }

            consumerRecords.forEach(record -> {
                System.out.printf("Consumer Record:(%s, %s, %d, %d)\n",
                        record.key(), record.value(),
                        record.partition(), record.offset());
            });

            consumer.commitAsync();
        }
        consumer.close();
        System.out.println("DONE");
    }
}
