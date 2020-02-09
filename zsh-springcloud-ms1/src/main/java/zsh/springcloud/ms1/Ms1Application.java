package zsh.springcloud.ms1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableFeignClients
@SpringBootApplication
public class Ms1Application {

	public static void main(String[] args) {
		SpringApplication.run(Ms1Application.class, args);
	}

	@Autowired
	private FeignHttpClient feignHttpClient;

	@GetMapping("aloha")
	public String msHttpCall() {
		return feignHttpClient.getAlohaFromMicroService2();
	}

}
