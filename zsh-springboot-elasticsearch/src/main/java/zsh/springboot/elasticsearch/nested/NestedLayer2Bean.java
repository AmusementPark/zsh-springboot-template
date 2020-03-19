package zsh.springboot.elasticsearch.nested;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder(toBuilder = true)
public class NestedLayer2Bean {
    private String l2String1;
    private String l2String2;

    @Field(type = FieldType.Nested)
    private NestedLayer3Bean layer3Bean;
}
