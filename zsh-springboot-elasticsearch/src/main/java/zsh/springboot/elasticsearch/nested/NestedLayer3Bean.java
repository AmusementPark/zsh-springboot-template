package zsh.springboot.elasticsearch.nested;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class NestedLayer3Bean {
    private String l3String1;
    private String l3String2;
}
