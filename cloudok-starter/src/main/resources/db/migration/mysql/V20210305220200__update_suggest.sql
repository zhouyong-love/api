ALTER TABLE `uc_member_suggest` 
ADD COLUMN `specialism` TINYINT(1) NULL COMMENT '0 完全不同 1 大类相同 2 细分相同' AFTER `score`,
ADD COLUMN `industry` TINYINT(1) NULL COMMENT '0 完全不同 1 大类相同 2 细分相同' AFTER `specialism`,
ADD COLUMN `tag` TINYINT(1) NULL COMMENT '0 完全不同 1 大类相同 2 细分相同' AFTER `industry`;
