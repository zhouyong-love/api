ALTER TABLE `uc_message_thread_members` 
ADD COLUMN `un_read` INT NULL DEFAULT 0 AFTER `last_position`;
