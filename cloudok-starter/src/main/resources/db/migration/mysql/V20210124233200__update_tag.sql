ALTER TABLE `base_tag` ADD COLUMN `parent_id` BIGINT(20) NULL COMMENT '父标签id' AFTER `id`;
