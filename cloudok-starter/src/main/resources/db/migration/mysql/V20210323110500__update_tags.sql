ALTER TABLE  `base_tag` ADD COLUMN `relation_to` BIGINT(20) NULL COMMENT '指向某一个tag id,用于动态推荐标签' AFTER `color`;

ALTER TABLE `bbs_comment` 
DROP COLUMN `status_ts`,
DROP COLUMN `status`;


ALTER TABLE `bbs_thumbs_up` 
DROP COLUMN `status_ts`,
DROP COLUMN `status`;
