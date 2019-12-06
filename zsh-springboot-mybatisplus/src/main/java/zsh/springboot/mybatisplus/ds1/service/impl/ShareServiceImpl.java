package zsh.springboot.mybatisplus.ds1.service.impl;

import zsh.springboot.mybatisplus.ds1.model.Share;
import zsh.springboot.mybatisplus.ds1.dao.ShareDao;
import zsh.springboot.mybatisplus.ds1.service.ShareService;
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
@Service
public class ShareServiceImpl extends ServiceImpl<ShareDao, Share> implements ShareService {

}
