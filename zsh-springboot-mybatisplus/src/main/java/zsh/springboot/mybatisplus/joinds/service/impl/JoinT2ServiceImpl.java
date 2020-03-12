package zsh.springboot.mybatisplus.joinds.service.impl;

import org.springframework.context.annotation.Profile;
import zsh.springboot.mybatisplus.joinds.model.JoinT2;
import zsh.springboot.mybatisplus.joinds.dao.JoinT2Dao;
import zsh.springboot.mybatisplus.joinds.service.JoinT2Service;
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
public class JoinT2ServiceImpl extends ServiceImpl<JoinT2Dao, JoinT2> implements JoinT2Service {

}
