package zsh.springboot.mybatisplus.multids.ds0.service.impl;

import org.springframework.context.annotation.Profile;
import zsh.springboot.mybatisplus.multids.ds0.model.ShareHolder;
import zsh.springboot.mybatisplus.multids.ds0.dao.ShareHolderDao;
import zsh.springboot.mybatisplus.multids.ds0.service.ShareHolderService;
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
public class ShareHolderServiceImpl extends ServiceImpl<ShareHolderDao, ShareHolder> implements ShareHolderService {

}
