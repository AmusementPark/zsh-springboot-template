package zsh.springboot.mybatisplus.joinds.service.impl;

import org.springframework.context.annotation.Profile;
import zsh.springboot.mybatisplus.joinds.model.JoinMain;
import zsh.springboot.mybatisplus.joinds.dao.JoinMainDao;
import zsh.springboot.mybatisplus.joinds.service.JoinMainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2020-03-12
 */
@Profile("join_ds")
@Service
public class JoinMainServiceImpl extends ServiceImpl<JoinMainDao, JoinMain> implements JoinMainService {

}
