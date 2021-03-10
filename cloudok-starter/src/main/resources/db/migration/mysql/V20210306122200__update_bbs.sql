ALTER TABLE  `bbs_thumbs_up` ADD INDEX `idx_tu_business_id` (`business_id` ASC);
ALTER TABLE `bbs_post` ADD INDEX `idx_post_topic` (`topic_id` ASC, `topic_type` ASC);

ALTER TABLE  `bbs_comment` 
ADD COLUMN `status` TINYINT(1) NULL DEFAULT 0 COMMENT '0 未读 1 已读' AFTER `parent_id`,
ADD COLUMN `status_ts` TIMESTAMP NULL COMMENT '读时间' AFTER `status`;

ALTER TABLE  `bbs_thumbs_up` 
ADD COLUMN `status` TINYINT(1) NULL DEFAULT 0 COMMENT '0 未读 1 已读' AFTER `business_id`,
ADD COLUMN `status_ts` TIMESTAMP NULL COMMENT '读时间' AFTER `status`;