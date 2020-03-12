package zsh.springboot.mybatisplus.ds1.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zsh.springboot.mybatisplus.ds1.dao.ShareDao;
import zsh.springboot.mybatisplus.ds1.model.Share;
import zsh.springboot.mybatisplus.ds1.service.ShareService;

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
public class ShareServiceImpl extends ServiceImpl<ShareDao, Share> implements ShareService {

    @Override
    @Transactional(transactionManager = "ds1DataSourceTxManager", rollbackFor = Throwable.class)
    public void insertTransactionalAndRollBack() {
        Share share = Share.builder().shareCode("900030").shareName("未知证券").build();
        save(share);
        throw new RuntimeException("Oh no... rollback");
    }
}
