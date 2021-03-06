ALTER TABLE `bbs_comment` 
ADD COLUMN `status` TINYINT(1) NULL DEFAULT 0 COMMENT '0 未读，1 已读' AFTER `parent_id`,
ADD COLUMN `status_ts` TIMESTAMP NULL COMMENT '读的时间' AFTER `status`;


ALTER TABLE `bbs_thumbs_up` 
ADD COLUMN `status` TINYINT(1) NULL DEFAULT 0 COMMENT '0 未读，1 已读' AFTER `business_id`,
ADD COLUMN `status_ts` TIMESTAMP NULL COMMENT '读的时间' AFTER `status`;


CREATE TABLE IF NOT EXISTS `uc_member_suggest` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `suggest_ts` TIMESTAMP NULL COMMENT '推荐时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `owner_id` BIGINT(20) NULL,
  `target_id` BIGINT(20) NULL,
  `score` DOUBLE NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '用户推荐评分';