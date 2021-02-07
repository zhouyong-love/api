ALTER TABLE `uc_message_thread` 
CHANGE COLUMN `ower_id` `owner_id` BIGINT(20) NULL DEFAULT NULL COMMENT '归属id，对于私信 不存在归属于谁，对于留言 留给谁的就是谁的' ;

ALTER TABLE  `uc_message_thread_members` 
ADD COLUMN `member_id` BIGINT(20) NULL AFTER `thread_id`;

ALTER TABLE  `uc_message` 
ADD COLUMN `member_id` BIGINT(20) NULL AFTER `thread_id`;
