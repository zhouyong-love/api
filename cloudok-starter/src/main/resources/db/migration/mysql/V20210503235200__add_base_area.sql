CREATE TABLE `base_area`(
  `id` bigint(20) NOT NULL,
  `tenant_id` BIGINT(20) NULL DEFAULT 0 COMMENT '租户id',
  `name` varchar(24) NOT NULL COMMENT '地区名称',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `sn` INT NULL DEFAULT 0,
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `create_ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学校地区表';