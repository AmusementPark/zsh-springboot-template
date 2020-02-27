package zsh.springboot.reactiveweb.ds0.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import zsh.springboot.reactiveweb.ds0.service.ShareHolderService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2019-12-06
 */
@RestController
@RequestMapping("ds")
public class ShareHolderController {
    @Autowired
    ShareHolderService shareHolderService;

    @RequestMapping("mono")
    public Mono<Void> doTransaction() {
        return shareHolderService.doTransaction();
    }
}

