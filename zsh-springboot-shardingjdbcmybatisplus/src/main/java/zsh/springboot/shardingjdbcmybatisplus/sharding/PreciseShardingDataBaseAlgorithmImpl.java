package zsh.springboot.shardingjdbcmybatisplus.sharding;

import groovy.util.logging.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

@Slf4j
public class PreciseShardingDataBaseAlgorithmImpl implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
//        System.out.println("PreciseShardingDataBaseAlgorithmImpl");
        if (availableTargetNames.size() == 1) {
            return (String) availableTargetNames.toArray()[0];
        }
        for (String each : availableTargetNames) {
            if (each.endsWith(shardingValue.getValue() % 4 + "")) {
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }
}
