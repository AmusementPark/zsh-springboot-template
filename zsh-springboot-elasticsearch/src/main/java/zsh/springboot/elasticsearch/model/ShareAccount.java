package zsh.springboot.elasticsearch.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: 韩世超
 * @Date: 2019/6/17 7:09
 * @Email: shichao.han@carota.ai
 * @Version 1.1.0
 */
@Data
@Builder
@Document(indexName = "idx_share_account", type = "type_share_account")
public class ShareAccount implements Serializable {

    private Long id;
    private String shareHolder;
    private Date createTime;
    private Date updateTime;

    @Field(type = FieldType.Nested)
    List<ShareBalance> shareBalances;
}
