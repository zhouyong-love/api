ALTER TABLE `uc_member_suggest` 
ADD COLUMN `school` TINYINT(1) NULL COMMENT '0 完全不同 1 学校相同' AFTER `tag`;
