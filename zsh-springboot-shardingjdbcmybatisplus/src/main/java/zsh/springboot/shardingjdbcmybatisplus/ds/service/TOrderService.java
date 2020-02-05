package zsh.springboot.shardingjdbcmybatisplus.ds.service;

import zsh.springboot.shardingjdbcmybatisplus.ds.model.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-05
 */
public interface TOrderService extends IService<TOrder> {
    void truncate();
}
