package zsh.springboot.elasticsearch.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Layer3Bean {
    private String l3String1;
    private String l3String2;
}
