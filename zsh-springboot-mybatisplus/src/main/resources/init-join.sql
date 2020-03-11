CREATE SCHEMA join_ds;

DROP TABLE IF EXISTS `join_ds`.`join_main`;
DROP TABLE IF EXISTS `join_ds`.`join_t1`;
DROP TABLE IF EXISTS `join_ds`.`join_t2`;
DROP TABLE IF EXISTS `join_ds`.`join_t3`;


CREATE TABLE `join_ds`.`join_main` (
  `id` bigint(20) NOT NULL,
  `join_t1_id` bigint(20) NOT NULL,
  `join_t2_id` bigint(20) NOT NULL,
  `join_t3_id` bigint(20) NOT NULL,
  `join_main_name` varchar(45) NOT NULL,
  `join_main_desc` varchar(45) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `join_ds`.`join_t1` (
  `id` bigint(20) NOT NULL,
  `join_t1_name` varchar(45) NOT NULL,
  `join_t1_desc` varchar(45) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `join_ds`.`join_t2` (
  `id` bigint(20) NOT NULL,
  `join_t3_name` varchar(45) NOT NULL,
  `join_t3_desc` varchar(45) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `join_ds`.`join_t3` (
  `id` bigint(20) NOT NULL,
  `join_t3_name` varchar(45) NOT NULL,
  `join_t3_desc` varchar(45) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
