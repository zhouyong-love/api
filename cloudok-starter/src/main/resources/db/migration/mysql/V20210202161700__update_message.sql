ALTER TABLE `uc_message` CHANGE COLUMN `thread_id` `thread_id` VARCHAR(50) NULL DEFAULT NULL COMMENT '回话id 两个人的私聊归属同过一个回话id，每次互动问和答算一个回话id' ;
