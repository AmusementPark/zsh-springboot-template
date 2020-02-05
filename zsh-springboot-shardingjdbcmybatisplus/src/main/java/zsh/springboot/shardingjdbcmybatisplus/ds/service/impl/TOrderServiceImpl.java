package zsh.springboot.shardingjdbcmybatisplus.ds.service.impl;

import zsh.springboot.shardingjdbcmybatisplus.ds.model.TOrder;
import zsh.springboot.shardingjdbcmybatisplus.ds.dao.TOrderDao;
import zsh.springboot.shardingjdbcmybatisplus.ds.service.TOrderService;
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
public class TOrderServiceImpl extends ServiceImpl<TOrderDao, TOrder> implements TOrderService {

    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }
}
