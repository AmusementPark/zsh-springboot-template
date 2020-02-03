package zsh.springboot.mybatisplus.ds1.service;

import zsh.springboot.mybatisplus.ds1.model.Share;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-06
 */
public interface ShareService extends IService<Share> {
    void insertTransactionalAndRollBack();
}
