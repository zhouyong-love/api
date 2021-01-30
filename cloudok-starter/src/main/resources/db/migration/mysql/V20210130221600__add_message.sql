CREATE TABLE IF NOT EXISTS  `uc_message` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `type` TINYINT(1) NULL COMMENT '类型： 1 认可消息 2 私信 3 匿名互动 4 实名互动',
  `from_id` BIGINT(20) NULL COMMENT '发送人id',
  `to_id` BIGINT(20) NULL COMMENT '接收人id',
  `content` VARCHAR(1024) NULL COMMENT '消息内容',
  `status` TINYINT(1) NULL COMMENT '状态 0 未读  1 已读',
  `status_ts` TIMESTAMP NULL COMMENT '状态变更时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '消息';