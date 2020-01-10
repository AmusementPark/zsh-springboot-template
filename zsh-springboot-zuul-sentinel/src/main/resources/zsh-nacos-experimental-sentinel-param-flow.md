| 字段| 说明| 默认值|
| ------ | ------ | ------ |
|resource|资源名，必填||
|count|限流阈值，必填||
|grade|限流模式|QPS 模式
|durationInSec	|统计窗口时间长度（单位为秒），1.6.0 版本开始支持|1s
|controlBehavior|流控效果（支持快速失败和匀速排队模式），1.6.0 版本开始支持|快速失败
|maxQueueingTimeMs|最大排队等待时长（仅在匀速排队模式生效），1.6.0 版本开始支持	|0ms
|paramIdx|热点参数的索引，必填，对应 SphU.entry(xxx, args) 中的参数索引位置||
|paramFlowItemList|参数例外项，可以针对指定的参数值单独设置限流阈值，不受前面count 阈值的限制。仅支持基本类型||
|clusterMode|是否是集群参数流控规则|false
|clusterConfig|集群流控相关配置||