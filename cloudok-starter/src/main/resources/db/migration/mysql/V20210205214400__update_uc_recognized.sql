ALTER TABLE `uc_recognized` 
ADD COLUMN `read` TINYINT(1) NOT NULL DEFAULT 0 AFTER `target_id`,
ADD COLUMN `read_time` TIMESTAMP NULL AFTER `read`;
