ALTER TABLE  `uc_message_thread` 
ADD COLUMN `last_message_id` BIGINT(20) NULL AFTER `owner_id`;
