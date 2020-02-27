package zsh.springboot.reactiveweb.ds0.service;

import reactor.core.publisher.Mono;
import zsh.springboot.reactiveweb.ds0.model.ShareHolder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2019-12-06
 */
public interface ShareHolderService extends IService<ShareHolder> {
    Mono<Void> doTransaction();
}
