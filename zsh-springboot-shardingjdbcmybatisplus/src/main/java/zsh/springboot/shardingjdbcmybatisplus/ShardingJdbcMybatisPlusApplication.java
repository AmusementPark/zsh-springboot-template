package zsh.springboot.shardingjdbcmybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zsh.springboot.shardingjdbcmybatisplus.ds.model.TOrder;
import zsh.springboot.shardingjdbcmybatisplus.ds.model.TOrderItem;
import zsh.springboot.shardingjdbcmybatisplus.ds.service.TOrderItemService;
import zsh.springboot.shardingjdbcmybatisplus.ds.service.TOrderService;

import java.util.ArrayList;
import java.util.List;

@MapperScan(value = "zsh.springboot.shardingjdbcmybatisplus.ds.dao*")
@SpringBootApplication
public class ShardingJdbcMybatisPlusApplication {

    @Autowired
    private TOrderService tOrderService;
    @Autowired
    private TOrderItemService tOrderItemService;

    public static void main(String[] args) {
        ShardingJdbcMybatisPlusApplication app = SpringApplication.run(ShardingJdbcMybatisPlusApplication.class, args).getBean(ShardingJdbcMybatisPlusApplication.class);
        app.truncate();
        app.insert();
    }

    public void insert() {
        List<Long> result = new ArrayList<>(10);
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
    }

    public void truncate() {
        tOrderService.truncate();
        tOrderItemService.truncate();
    }
}
