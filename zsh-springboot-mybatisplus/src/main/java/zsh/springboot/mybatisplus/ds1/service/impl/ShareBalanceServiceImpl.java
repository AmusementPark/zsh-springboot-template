package zsh.springboot.mybatisplus.ds1.service.impl;

import org.springframework.context.annotation.Profile;
import zsh.springboot.mybatisplus.ds1.model.ShareBalance;
import zsh.springboot.mybatisplus.ds1.dao.ShareBalanceDao;
import zsh.springboot.mybatisplus.ds1.service.ShareBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-06
 */
@Profile("multi-ds")
@Service
public class ShareBalanceServiceImpl extends ServiceImpl<ShareBalanceDao, ShareBalance> implements ShareBalanceService {

}
