package zsh.springboot.shardingjdbcmybatisplus.ds.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import zsh.springboot.shardingjdbcmybatisplus.ds.model.TOrderItem;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2020-02-05
 */
@Mapper
public interface TOrderItemDao extends BaseMapper<TOrderItem> {
//    @Update("TRUNCATE TABLE t_order_item")
    void truncate();
}
