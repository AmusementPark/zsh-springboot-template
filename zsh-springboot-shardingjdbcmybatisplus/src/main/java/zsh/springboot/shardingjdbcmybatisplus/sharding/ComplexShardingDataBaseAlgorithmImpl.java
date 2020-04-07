//package zsh.springboot.shardingjdbcmybatisplus.sharding;
//
//import com.google.common.base.Charsets;
//import com.google.common.hash.Hashing;
//import groovy.util.logging.Slf4j;
//import io.shardingsphere.core.api.algorithm.sharding.ListShardingValue;
//import io.shardingsphere.core.api.algorithm.sharding.ShardingValue;
//import io.shardingsphere.core.api.algorithm.sharding.complex.ComplexKeysShardingAlgorithm;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//
//@Slf4j
//public class ComplexShardingDataBaseAlgorithmImpl implements ComplexKeysShardingAlgorithm {
//
//    @Override
//    public Collection<String> doSharding(Collection<String> collection, Collection<ShardingValue> shardingValues) {
//        Collection<String> vins     = getShardingValue(shardingValues, "vin");
//        Collection<String> orderNos = getShardingValue(shardingValues, "order_no");
//        List<String> collections = new ArrayList<>();
//        /**例如：根据user_id + order_id 双分片键来进行分表*/
//        //Set<List<Integer>> valueResult = Sets.cartesianProduct(userIdValues, orderIdValues);
//        for (String vin : vins) {
//            for (String orderNo : orderNos) {
//                int hash = Hashing.murmur3_32().newHasher()
//                    .putString(vin, Charsets.UTF_8)
//                    .putString(orderNo, Charsets.UTF_8)
//                    .hash().asInt();
//                int locate = Hashing.consistentHash(hash, collection.size());
//                for (String database : collection) {
//                    if (database.endsWith(locate+"")) {
//                        collections.add(database);
//                    }
//                }
//            }
//        }
//        // 如果只出现了vin或orderNo其中之一，这里返回就有问题了
//        return collections;
//    }
//
//    private Collection<String> getShardingValue(Collection<ShardingValue> shardingValues, final String key) {
//        Collection<String> valueSet = new ArrayList<>();
//        Iterator<ShardingValue> iterator = shardingValues.iterator();
//        while (iterator.hasNext()) {
//            ShardingValue next = iterator.next();
//            if (next instanceof ListShardingValue) {
//                ListShardingValue value = (ListShardingValue) next;
//                /**例如：根据user_id + order_id 双分片键来进行分表*/
//                if (value.getColumnName().equals(key)) {
//                    return value.getValues();
//                }
//            }
//        }
//        return valueSet;
//    }valueSet
//
////    public void testConsistentHash() {
////        List<String> ips = Lists.newArrayList("192.168.1.100", "192.168.1.110", "192.168.1.120");
////        long ipHashCode1 = Hashing.murmur3_32().newHasher().putString(ips.get(0), Charsets.UTF_8).hash().asLong();
////        long ipHashCode2 = Hashing.murmur3_32().newHasher().putString(ips.get(1), Charsets.UTF_8).hash().asLong();
////        long ipHashCode3 = Hashing.murmur3_32().newHasher().putString(ips.get(2), Charsets.UTF_8).hash().asLong();
////
////        System.out.println("ip1: " + Hashing.consistentHash(ipHashCode1, 3));
////        System.out.println("ip2: " + Hashing.consistentHash(ipHashCode2, 3));
////        System.out.println("ip3: " + Hashing.consistentHash(ipHashCode3, 3));
////    }
//}
