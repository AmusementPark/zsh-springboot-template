package zsh.springboot.shardingjdbcmybatisplus.sharding;

import org.apache.shardingsphere.api.hint.HintManager;
import org.apache.shardingsphere.shardingjdbc.spring.boot.masterslave.MasterSlaveRuleCondition;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * Sharing-JDBC模式为读写分离时，仅支持:
 * https://shardingsphere.apache.org/document/current/cn/features/read-write-split/core-features/
 * 同一线程且同一数据库连接内，如有写入操作，以后的读操作均从主库读取，用于保证数据一致性。
 *
 * 为了能使事务能在主库上操作，添加这个切面强制@Transational路由主库
 */
@Aspect
@Component
@Conditional({MasterSlaveRuleCondition.class})
public class ShardingMasterSlaveTransactionalInterceptor {
    private static final String POINT = "@annotation(org.springframework.transaction.annotation.Transactional)";

    @Before(POINT)
    public void before() {
        if (!HintManager.isMasterRouteOnly()) {
            HintManager.clear();
            HintManager.getInstance().setMasterRouteOnly();
        }
    }

    @After(POINT)
    public void after() {
        HintManager.clear();
    }
}
