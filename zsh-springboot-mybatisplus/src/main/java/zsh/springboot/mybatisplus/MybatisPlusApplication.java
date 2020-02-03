package zsh.springboot.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zsh.springboot.mybatisplus.ds0.model.ShareHolder;
import zsh.springboot.mybatisplus.ds0.service.ShareHolderService;
import zsh.springboot.mybatisplus.ds1.model.Share;
import zsh.springboot.mybatisplus.ds1.service.ShareService;

@SpringBootApplication
public class MybatisPlusApplication {

    @Autowired
    private ShareHolderService shareHolderService;
    @Autowired
    private ShareService shareService;

    public static void main(String[] args) {
        MybatisPlusApplication app = SpringApplication.run(MybatisPlusApplication.class, args).getBean(MybatisPlusApplication.class);
        /**
         * INSERT
         */
//        app.shareService.save(Share.builder().shareCode("600030").shareName("中信证券").build());
//        app.shareService.save(Share.builder().shareCode("600000").shareName("浦发银行").build());
//        app.shareHolderService.save(ShareHolder.builder().username("88021927").password("99990099").build());
        /**
         * UPDATE
         */
//        UpdateWrapper<Share> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.set("share_code", "000002").set("share_name", "万科企业").eq("share_code", "000002");
//        app.shareService.update(updateWrapper);
        /**
         * TRANSACTIONAL
         */
        app.shareService.insertTransactionalAndRollBack();
    }

}
