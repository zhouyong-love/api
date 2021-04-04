ALTER TABLE `uc_member_suggest` 
ADD COLUMN `kab` DECIMAL(10,9) NULL DEFAULT 1 AFTER `school`,
ADD COLUMN `recognized` TINYINT(1) NULL AFTER `kab`;