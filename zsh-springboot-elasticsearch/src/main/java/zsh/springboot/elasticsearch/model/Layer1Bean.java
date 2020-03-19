package zsh.springboot.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@Document(indexName = "idx_sample", type = "type_sample")
public class Layer1Bean {
    private Long id;
    private String l1String1;
    private String l1String2;
    private List<Layer2Bean> layer2BeansList;
}
