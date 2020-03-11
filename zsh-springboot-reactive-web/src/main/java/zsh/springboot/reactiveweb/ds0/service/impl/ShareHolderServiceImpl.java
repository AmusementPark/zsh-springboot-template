package zsh.springboot.reactiveweb.ds0.service.impl;

import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import zsh.springboot.reactiveweb.ds0.model.ShareHolder;
import zsh.springboot.reactiveweb.ds0.dao.ShareHolderDao;
import zsh.springboot.reactiveweb.ds0.service.ShareHolderService;
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
public class ShareHolderServiceImpl extends ServiceImpl<ShareHolderDao, ShareHolder> implements ShareHolderService {

    @Transactional
    public Mono<Void> doTransaction() {
        System.out.println(Thread.currentThread().getId());
        return Mono.just(ShareHolder.builder().username("USERNAME").password("PASSWORD").build())
            .flatMap(shareHolder -> {
                System.out.println(Thread.currentThread().getId());
                this.baseMapper.insert(shareHolder);
                return Mono.just("STRSTR");
            })
            .flatMap(mono -> {
                try {
                    Thread.sleep(1000);
                    System.out.println("XXXXXX");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return Mono.empty();
            });
    }
}
