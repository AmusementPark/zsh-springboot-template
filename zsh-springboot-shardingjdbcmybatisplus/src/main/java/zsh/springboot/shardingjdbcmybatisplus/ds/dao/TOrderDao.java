package zsh.springboot.shardingjdbcmybatisplus.ds.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import zsh.springboot.shardingjdbcmybatisplus.ds.model.TOrder;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2020-02-05
 */
@Mapper
public interface TOrderDao extends BaseMapper<TOrder> {
//    @Update("TRUNCATE TABLE t_order")
    void truncate();
}
