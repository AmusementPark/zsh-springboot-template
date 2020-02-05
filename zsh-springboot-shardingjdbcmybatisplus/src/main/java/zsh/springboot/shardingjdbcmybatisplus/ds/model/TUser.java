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
@TableName("t_user")
public class TUser extends Model<TUser> {

    private static final long serialVersionUID=1L;

      @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @TableField("user_name")
    private String userName;

    @TableField("user_name_plain")
    private String userNamePlain;

    @TableField("pwd")
    private String pwd;

    @TableField("assisted_query_pwd")
    private String assistedQueryPwd;


    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
