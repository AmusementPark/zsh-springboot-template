package zsh.springboot.shardingjdbcmybatisplus.sharding;

import org.apache.shardingsphere.api.sharding.ShardingValue;
import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;

import java.util.Collection;

public class HintShardingAlgorithmImpl implements HintShardingAlgorithm {

    @Override
    public Collection<String> doSharding(Collection collection, HintShardingValue hintShardingValue) {
        System.out.println("HintShardingAlgorithmImpl");
        return null;
    }
}
