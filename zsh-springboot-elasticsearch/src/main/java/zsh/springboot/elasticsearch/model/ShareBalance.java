package zsh.springboot.elasticsearch.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder(toBuilder = true)
public class ShareBalance implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String shareCode;
    private String shareName;
    private String shareBalance;
}
