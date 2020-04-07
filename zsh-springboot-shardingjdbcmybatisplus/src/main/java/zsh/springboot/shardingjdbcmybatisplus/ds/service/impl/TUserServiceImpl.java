package zsh.springboot.shardingjdbcmybatisplus.ds.service.impl;

import zsh.springboot.shardingjdbcmybatisplus.ds.model.TUser;
import zsh.springboot.shardingjdbcmybatisplus.ds.dao.TUserDao;
import zsh.springboot.shardingjdbcmybatisplus.ds.service.TUserService;
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
public class TUserServiceImpl extends ServiceImpl<TUserDao, TUser> implements TUserService {
    @Override
    public void truncate() {
        this.baseMapper.truncate();
    }
}
