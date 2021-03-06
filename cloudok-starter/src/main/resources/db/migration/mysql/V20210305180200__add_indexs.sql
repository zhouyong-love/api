ALTER TABLE `uc_member_suggest` 
ADD INDEX `idx_ms_owner_id` (`owner_id` ASC),
ADD INDEX `idx_ms_target_id` (`target_id` ASC);

ALTER TABLE `uc_message_thread` 
ADD INDEX `idx_mt_owner_id` (`owner_id` ASC),
ADD INDEX `idx_mt_type` (`type` ASC);


ALTER TABLE `uc_message_thread_members` 
ADD INDEX `idx_mtm_member_id` (`member_id` ASC);
