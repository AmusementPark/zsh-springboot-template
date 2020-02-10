package zsh.springcloud.ms1;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@EnableFeignClients
@SpringBootApplication
public class Ms1Application {

	public static void main(String[] args) {
		SpringApplication.run(Ms1Application.class, args);
	}

	@Autowired
	private FeignHttpClient feignHttpClient;
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@GetMapping("aloha")
	public String msHttpCall() {
		/**
		 * 可以支持KAFKA消息的追踪
		 */
		ProducerRecord<String, String> record = new ProducerRecord<>(
			"T2",
			RandomUtils.nextInt(4),
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
		return feignHttpClient.getAlohaFromMicroService2();
	}

}
