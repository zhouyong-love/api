CREATE TABLE `uc_notification` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tenant_id` bigint(20) DEFAULT '1' COMMENT '租户id',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人id',
  `create_ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `business_type` tinyint(1) DEFAULT NULL COMMENT '业务类型： 1 评论 2 点赞',
  `business_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL COMMENT '提醒摘要',
  `remark` varchar(1024) DEFAULT NULL  COMMENT '提醒详情',
  `status` tinyint(1) DEFAULT '0' COMMENT '0 未读 1 已读',
  `status_ts` timestamp NULL DEFAULT NULL COMMENT '读时间',
  PRIMARY KEY (`id`),
  KEY `idx_un_business_id` (`business_id`),
  KEY `idx_un_business_type` (`business_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='消息提醒';