package zsh.springcloud.ms2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@SpringBootApplication
public class Ms2Application {

    public static void main(String[] args) {
        Ms2Application ms2Application = SpringApplication.run(Ms2Application.class, args).getBean(Ms2Application.class);
    }

    @RequestMapping("get")
    public String alohaFromMicroService2() {
        return "Aloha From MS2!";
    }
}
