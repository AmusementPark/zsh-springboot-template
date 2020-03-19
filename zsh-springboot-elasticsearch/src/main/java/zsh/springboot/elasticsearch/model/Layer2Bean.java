package zsh.springboot.elasticsearch.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Layer2Bean {
    private String l2String1;
    private String l2String2;
    private Layer3Bean layer3Bean;
}
