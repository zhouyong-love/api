ALTER TABLE `bbs_comment` ADD COLUMN `reply_to` BIGINT(20) NULL AFTER `status_ts`;
