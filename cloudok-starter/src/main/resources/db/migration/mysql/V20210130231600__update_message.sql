ALTER TABLE `uc_message` ADD COLUMN `thread_id` BIGINT(20) NULL COMMENT '回话id 两个人的私聊归属同过一个回话id，每次互动问和答算一个回话id' AFTER `type`;
