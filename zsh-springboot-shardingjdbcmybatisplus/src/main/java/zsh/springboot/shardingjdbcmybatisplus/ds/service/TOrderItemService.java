package zsh.springboot.shardingjdbcmybatisplus.ds.service;

import zsh.springboot.shardingjdbcmybatisplus.ds.model.TOrderItem;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-05
 */
public interface TOrderItemService extends IService<TOrderItem> {
    void truncate();
}
