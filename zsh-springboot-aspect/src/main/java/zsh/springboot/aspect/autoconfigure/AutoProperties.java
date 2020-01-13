package zsh.springboot.aspect.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "zsh.springboot.template")
public class AutoProperties {
    private Map<String, String> custom;
}
