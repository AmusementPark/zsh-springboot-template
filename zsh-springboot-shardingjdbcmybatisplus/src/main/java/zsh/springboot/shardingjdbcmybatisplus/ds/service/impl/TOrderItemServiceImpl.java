package zsh.springboot.shardingjdbcmybatisplus.ds.service.impl;

import zsh.springboot.shardingjdbcmybatisplus.ds.model.TOrderItem;
import zsh.springboot.shardingjdbcmybatisplus.ds.dao.TOrderItemDao;
import zsh.springboot.shardingjdbcmybatisplus.ds.service.TOrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-02-05
 */
@Service
public class TOrderItemServiceImpl extends ServiceImpl<TOrderItemDao, TOrderItem> implements TOrderItemService {

    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }
}
