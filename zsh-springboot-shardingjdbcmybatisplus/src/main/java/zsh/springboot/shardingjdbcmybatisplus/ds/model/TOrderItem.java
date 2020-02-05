package zsh.springboot.shardingjdbcmybatisplus.ds.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * @since 2020-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_order_item")
public class TOrderItem extends Model<TOrderItem> {

    private static final long serialVersionUID=1L;

      @TableId(value = "order_item_id", type = IdType.AUTO)
    private Long orderItemId;

    @TableField("order_id")
    private Long orderId;

    @TableField("user_id")
    private Integer userId;

    @TableField("status")
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.orderItemId;
    }

}
