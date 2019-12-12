package zsh.springboot.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
//    @Value("${spring.kafka.consumer.group-id}")
//    private String groupId;
    @Value("${spring.kafka.consumer.max-poll-records}")
    private String maxPollRecords;
    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;
    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private String enableAutoCommit;
    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private String autoCommitInterval;
    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;
    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;
    @Value("${spring.kafka.batch}")
    private boolean batch;
    @Value("${spring.kafka.concurrency}")
    private int concurrency;
    @Value("${spring.kafka.pollTimeOut}")
    private int pollTimeOut;
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,autoOffsetReset);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,enableAutoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,autoCommitInterval);
        return props;
    }

    @Bean
    public ConsumerFactory consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory kafkaListenerContainerFactory (
//            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
            KafkaTemplate kafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
//        factory.setRetryTemplate(retryTemplate());
//        factory.setBatchListener(batch);
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setPollTimeout(pollTimeOut);
//        factory.setBatchErrorHandler(new BatchLoggingErrorHandler());
        // 设置成手动模式
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setMissingTopicsFatal(false);  // 新版本设置不检查TOPIC是否存在

//        configurer.configure(factory, consumerFactory());
        // 最大重试三次
//        factory.setErrorHandler(new SeekToCurrentErrorHandler());
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate), 3));
//        factory.setBatchErrorHandler(new SeekToCurrentBatchErrorHandler());

        return factory;
    }


//    @KafkaListener(
//            id = "lc-ota-001-listener-data",
//            topics = {"lcOta001-data-test"}
//    )
//    public void receiveLcOta001ByData(List<ConsumerRecord<String, String>> messages) {
//        messages.forEach(consumerRecord -> {
//            System.out.println(consumerRecord.key()+ ":" +consumerRecord.value());
//        });
//    }

//    @Bean
//    public RetryTemplate retryTemplate() {
//
//        RetryListener rl = new RetryListener() {
//            @Override
//            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
//                return false;
//            }
//
//            @Override
//            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
//                System.out.println("CLOSE");
//            }
//
//            @Override
//            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
//                System.out.println("onError");
//            }
//        };
//
//        final ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
//        backOffPolicy.setInitialInterval(1000);
//        backOffPolicy.setMaxInterval(5000);
////        backOffPolicy.setMultiplier(1.5);
//
//        final RetryTemplate template = new RetryTemplate();
//        template.setRetryPolicy(new SimpleRetryPolicy(3));
//        template.setBackOffPolicy(backOffPolicy);
//        template.setListeners(new RetryListener[]{rl});
//
//        return template;
//    }

//    @Bean("SeekToCurrentErrorHandler")
//    public SeekToCurrentErrorHandler seekToCurrentErrorHandler(KafkaTemplate template) {
//        return new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(template), 3);
//    }

}
