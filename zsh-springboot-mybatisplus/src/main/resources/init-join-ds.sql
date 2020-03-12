DROP SCHEMA IF EXISTS join_ds;
CREATE SCHEMA join_ds;

DROP TABLE IF EXISTS `join_ds`.`join_main`;
DROP TABLE IF EXISTS `join_ds`.`join_t1`;
DROP TABLE IF EXISTS `join_ds`.`join_t2`;
DROP TABLE IF EXISTS `join_ds`.`join_t3`;
DROP TABLE IF EXISTS `join_ds`.`join_t4`;

CREATE TABLE `join_ds`.`join_main` (
  `id` bigint(20) NOT NULL,
  `join_t1_id` bigint(20) NOT NULL,
  `join_t2_id` bigint(20) NOT NULL,
  `join_t3_id` bigint(20) NOT NULL,
  `join_t4_id` bigint(20) NOT NULL,
  `join_main_name` varchar(45) NOT NULL,
  `join_main_desc` varchar(45) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
  , INDEX `idx_join_t1_id`(`join_t1_id`) USING BTREE
  , INDEX `idx_join_t2_id`(`join_t2_id`) USING BTREE
  , INDEX `idx_join_t3_id`(`join_t3_id`) USING BTREE
  , INDEX `idx_join_t4_id`(`join_t4_id`) USING BTREE
  -- , INDEX `idx_join_union_all`(`join_t1_id`,`join_t2_id`,`join_t3_id`,`join_t4_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `join_ds`.`join_t1` (
  `id` bigint(20) NOT NULL,
  `join_t1_id` bigint(20) NOT NULL,
  `join_t1_name` varchar(45) NOT NULL,
  `join_t1_desc` varchar(45) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
  , INDEX `idx_join_t1_id`(`join_t1_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `join_ds`.`join_t2` (
  `id` bigint(20) NOT NULL,
  `join_t2_id` bigint(20) NOT NULL,
  `join_t2_name` varchar(45) NOT NULL,
  `join_t2_desc` varchar(45) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
  , INDEX `idx_join_t2_id`(`join_t2_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `join_ds`.`join_t3` (
  `id` bigint(20) NOT NULL,
  `join_t3_id` bigint(20) NOT NULL,
  `join_t3_name` varchar(45) NOT NULL,
  `join_t3_desc` varchar(45) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
  , INDEX `idx_join_t3_id`(`join_t3_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `join_ds`.`join_t4` (
  `id` bigint(20) NOT NULL,
  `join_t4_id` bigint(20) NOT NULL,
  `join_t4_name` varchar(45) NOT NULL,
  `join_t4_desc` varchar(45) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
  , INDEX `idx_join_t4_id`(`join_t4_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 测试四张连表走索引查询
EXPLAIN SELECT
JM.id, JM.join_t1_id, JM.join_main_name,
J1.id, J1.join_t1_id, J1.join_t1_name,
J2.id, J2.join_t2_id, J2.join_t2_name,
J3.id, J3.join_t3_id, J3.join_t3_name,
J4.id, J4.join_t4_id, J4.join_t4_name
FROM `join_main` AS JM
JOIN `join_t1` AS J1 ON JM.join_t1_id=J1.join_t1_id
JOIN `join_t2` AS J2 ON JM.join_t2_id=J2.join_t2_id
JOIN `join_t3` AS J3 ON JM.join_t3_id=J3.join_t3_id
JOIN `join_t4` AS J4 ON JM.join_t4_id=J4.join_t4_id
WHERE JM.join_t1_id='29999747';
