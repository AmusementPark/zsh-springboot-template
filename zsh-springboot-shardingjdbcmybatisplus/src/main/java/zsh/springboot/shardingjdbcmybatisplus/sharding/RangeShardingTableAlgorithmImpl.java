package zsh.springboot.shardingjdbcmybatisplus.sharding;

import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RangeShardingTableAlgorithmImpl implements RangeShardingAlgorithm<Long> {
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {
        System.out.println("RANGE SHARDING");
//        List<String> list = new ArrayList<>();
//        list.add((String)availableTargetNames.toArray()[0]);
//        return list;
        return availableTargetNames;
    }
}
