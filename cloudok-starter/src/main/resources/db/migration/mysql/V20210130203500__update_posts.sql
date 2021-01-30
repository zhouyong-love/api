ALTER TABLE  `bbs_post` ADD COLUMN `attach_ids` VARCHAR(1024) NULL COMMENT 'post 图片' AFTER `collect_count`;

ALTER TABLE `bbs_post` ADD COLUMN `is_public` TINYINT(1) NULL DEFAULT 1 COMMENT '是否公开1是0否' AFTER `collect_count`;


CREATE TABLE IF NOT EXISTS `bbs_collect` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `business_type` TINYINT(1) NULL COMMENT '业务类型： 1 动态 2 评论',
  `business_id` BIGINT(20) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '收藏';

 
CREATE TABLE IF NOT EXISTS `bbs_thumbs_up` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `business_type` TINYINT(1) NULL COMMENT '业务类型： 1 动态 2 评论',
  `business_id` BIGINT(20) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '点赞';
