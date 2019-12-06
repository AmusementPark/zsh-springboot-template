package zsh.springboot.mybatisplus.ds1.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ${author}
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("share")
@Builder(toBuilder = true)
public class Share extends Model<Share> {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("share_code")
    private String shareCode;

    @TableField("share_name")
    private String shareName;

    @TableField("create_time")
    private Date createTime;

    @TableField(value = "update_time", update ="now()")
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
