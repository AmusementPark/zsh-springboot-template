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
@TableName("t_address")
public class TAddress extends Model<TAddress> {

    private static final long serialVersionUID=1L;

      @TableId(value = "address_id", type = IdType.ASSIGN_ID)
    private Long addressId;

    @TableField("address_name")
    private String addressName;


    @Override
    protected Serializable pkVal() {
        return this.addressId;
    }

}
