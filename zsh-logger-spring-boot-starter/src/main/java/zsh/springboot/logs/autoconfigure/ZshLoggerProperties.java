package zsh.springboot.logs.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("zsh.log")
public class ZshLoggerProperties {
    private List<String> excludePrintClass;
}
