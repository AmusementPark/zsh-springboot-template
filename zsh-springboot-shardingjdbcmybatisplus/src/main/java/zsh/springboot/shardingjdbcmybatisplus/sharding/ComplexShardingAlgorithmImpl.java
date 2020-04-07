//package zsh.springboot.shardingjdbcmybatisplus.sharding;
//
//import io.shardingsphere.core.api.algorithm.sharding.ListShardingValue;
//import io.shardingsphere.core.api.algorithm.sharding.ShardingValue;
//import io.shardingsphere.core.api.algorithm.sharding.complex.ComplexKeysShardingAlgorithm;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//
//public class ComplexShardingAlgorithmImpl implements ComplexKeysShardingAlgorithm {
//    @Override
//    public Collection<String> doSharding(Collection<String> collection, Collection<ShardingValue> shardingValues) {
//        Collection<Long> orderIdValues = getShardingValue(shardingValues, "order_id");
//        Collection<Long> userIdValues  = getShardingValue(shardingValues, "user_id");
//        List<String> shardingSuffix = new ArrayList<>();
//        /**例如：根据user_id + order_id 双分片键来进行分表*/
//        //Set<List<Integer>> valueResult = Sets.cartesianProduct(userIdValues, orderIdValues);
//        for (Long userIdVal : userIdValues) {
//            for (Long orderIdVal : orderIdValues) {
//                String suffix = userIdVal % 2 + "_" + orderIdVal % 2;
//                for (String x : collection) {
//                    if (x.endsWith(suffix)) {
//                        shardingSuffix.add(x);
//                    }
//                }
//            }
//        }
//
//        return shardingSuffix;
//    }
//
//    private Collection<Long> getShardingValue(Collection<ShardingValue> shardingValues, final String key) {
//        Collection<Long> valueSet = new ArrayList<>();
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
//    }
//}
