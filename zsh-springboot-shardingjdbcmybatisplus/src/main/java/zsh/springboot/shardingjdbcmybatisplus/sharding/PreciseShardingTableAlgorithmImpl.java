package zsh.springboot.shardingjdbcmybatisplus.sharding;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public class PreciseShardingTableAlgorithmImpl implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
//        System.out.println("PreciseShardingTableAlgorithmImpl");
//        HashFunction hashFunction = Hashing.murmur3_32();
//        int hash = hashFunction.hashString(shardingValue.getValue(), Charsets.UTF_8).asInt();
//        HashCode hashCode = HashCode.fromInt(hash);
//        int locate = Hashing.consistentHash(hashCode, availableTargetNames.size());
//        return (String) availableTargetNames.toArray()[locate];
        Long mod = shardingValue.getValue()%2;
        return (String)availableTargetNames.toArray()[mod.intValue()];
    }
}
