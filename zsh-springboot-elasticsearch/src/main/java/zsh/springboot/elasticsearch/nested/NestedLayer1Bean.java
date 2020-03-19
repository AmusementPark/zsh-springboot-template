package zsh.springboot.elasticsearch.nested;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@Document(indexName = "idx_sample", type = "type_sample")
public class NestedLayer1Bean {
    private Long id;
    private String l1String1;
    private String l1String2;

    @Field(type = FieldType.Nested)
    private List<NestedLayer2Bean> layer2BeansList;
}
