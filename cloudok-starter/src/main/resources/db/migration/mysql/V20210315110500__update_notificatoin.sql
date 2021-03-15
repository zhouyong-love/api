ALTER TABLE `uc_notification` ADD COLUMN `member_id` BIGINT(20) NULL AFTER `update_ts`;

ALTER TABLE `uc_notification` ADD INDEX `idx_un_member_id` (`member_id` ASC);

