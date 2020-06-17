package zsh.springboot.shardingjdbcmybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import zsh.springboot.shardingjdbcmybatisplus.ds.model.TAddress;
import zsh.springboot.shardingjdbcmybatisplus.ds.model.TOrder;
import zsh.springboot.shardingjdbcmybatisplus.ds.model.TOrderItem;
import zsh.springboot.shardingjdbcmybatisplus.ds.model.TUser;
import zsh.springboot.shardingjdbcmybatisplus.ds.service.TAddressService;
import zsh.springboot.shardingjdbcmybatisplus.ds.service.TOrderItemService;
import zsh.springboot.shardingjdbcmybatisplus.ds.service.TOrderService;
import zsh.springboot.shardingjdbcmybatisplus.ds.service.TUserService;

import java.util.ArrayList;
import java.util.List;

@MapperScan(value = "zsh.springboot.shardingjdbcmybatisplus.ds.dao*")
@SpringBootApplication
public class ShardingJdbcMybatisPlusApplication {

    @Autowired
    private TOrderService tOrderService;
    @Autowired
    private TOrderItemService tOrderItemService;
    @Autowired
    private TAddressService tAddressService;
    @Autowired
    private TUserService tUserService;

    public static void main(String[] args) {
        ShardingJdbcMybatisPlusApplication app = SpringApplication.run(ShardingJdbcMybatisPlusApplication.class, args).getBean(ShardingJdbcMybatisPlusApplication.class);
//        app.truncate();
//        app.insertUser();
//        app.insert();
        app.select();
//        app.selectInClause();
    }

    @Transactional(rollbackFor = Throwable.class)
    public void insert() {
        List<TOrder> orderList = tOrderService.list();
        System.out.println(orderList);
        List<Long> result = new ArrayList<>(2);
        for (int i = 1; i <= 10; i++) {
            TOrder order = new TOrder();
            order.setUserId(i);
            order.setAddressId(Long.valueOf(i));
            order.setStatus("INSERT_TEST");
            tOrderService.save(order);
            TOrderItem item = new TOrderItem();
            item.setOrderId(order.getOrderId());
            item.setUserId(i);
            item.setStatus("INSERT_TEST");
            tOrderItemService.save(item);
            result.add(order.getOrderId());
        }
        List<TOrder> list = tOrderService.list();

        TAddress tAddress = new TAddress();
        tAddress.setAddressName("aaaaa");
        tAddressService.save(tAddress);
        System.out.println(list);
    }

    public void insertUser() {
        List<TUser> list= new ArrayList<>();
        for (int i=0; i< 1000;i++) {
            TUser tUser = new TUser();
            String username = RandomStringUtils.randomAlphabetic(8).toUpperCase();
            tUser.setUserName(username);
            tUser.setUserNamePlain(username);
            tUser.setAssistedQueryPwd(username);
            tUser.setPwd(username);
            list.add(tUser);
        }
        tUserService.saveBatch(list);
    }

    public void select() {
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("order_id").gt("order_id", 452100248752861185L);
        List<TOrder> orderList = tOrderService.list(queryWrapper);
        System.out.println(orderList);
//        List<TOrderItem> orderItemList = tOrderItemService.list();
//        System.out.println(orderItemList);
    }

    public void selectInClause() {
        QueryWrapper<TOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("order_id", 452038302024314881L, 452038303718813697L);
        List<TOrder> orderList = tOrderService.list(queryWrapper);
        System.out.println(orderList);
    }

    public void truncate() {
        tUserService.truncate();
        tOrderService.truncate();
        tOrderItemService.truncate();
    }
}
