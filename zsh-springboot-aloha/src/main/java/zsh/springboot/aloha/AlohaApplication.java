package zsh.springboot.aloha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import zsh.springboot.importbean.EnableZshBean;

@EnableZshBean
@SpringBootApplication
public class AlohaApplication {

	@Autowired
	private ZshBeanInterface zshBeanInterface;

	public static void main(String[] args) {
		AlohaApplication alohaApplication = SpringApplication.run(AlohaApplication.class, args).getBean(AlohaApplication.class);
		alohaApplication.zshBeanInterface.print("1231231");
	}

}
