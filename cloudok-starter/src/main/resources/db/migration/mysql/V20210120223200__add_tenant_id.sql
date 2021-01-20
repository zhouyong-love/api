ALTER TABLE `sys_attach` ADD COLUMN `tenant_id` BIGINT(20) NULL AFTER `update_ts`;
ALTER TABLE `sys_dict` ADD COLUMN `tenant_id` BIGINT(20) NULL AFTER `update_ts`;
ALTER TABLE `sys_dict_data` ADD COLUMN `tenant_id` BIGINT(20) NULL AFTER `update_ts`;
ALTER TABLE `sys_obj_role` ADD COLUMN `tenant_id` BIGINT(20) NULL AFTER `update_ts`;
ALTER TABLE `sys_resource` ADD COLUMN `tenant_id` BIGINT(20) NULL AFTER `update_ts`;
ALTER TABLE `sys_role` ADD COLUMN `tenant_id` BIGINT(20) NULL AFTER `update_ts`;
ALTER TABLE `sys_role_res` ADD COLUMN `tenant_id` BIGINT(20) NULL AFTER `update_ts`;
ALTER TABLE `sys_user` ADD COLUMN `tenant_id` BIGINT(20) NULL AFTER `update_ts`;
