ALTER TABLE `uc_member` 
ADD COLUMN `profile_update_ts` TIMESTAMP NULL COMMENT '个人名片最后更新时间' AFTER `update_ts`;

update `uc_member`  set profile_update_ts =  create_ts;