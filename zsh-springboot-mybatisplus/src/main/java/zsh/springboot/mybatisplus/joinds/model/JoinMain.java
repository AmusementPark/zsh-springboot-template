package zsh.springboot.mybatisplus.joinds.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2020-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("join_main")
public class JoinMain extends Model<JoinMain> {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("join_t1_id")
    private Long joinT1Id;

    @TableField("join_t2_id")
    private Long joinT2Id;

    @TableField("join_t3_id")
    private Long joinT3Id;

    @TableField("join_t4_id")
    private Long joinT4Id;

    @TableField("join_main_name")
    private String joinMainName;

    @TableField("join_main_desc")
    private String joinMainDesc;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
