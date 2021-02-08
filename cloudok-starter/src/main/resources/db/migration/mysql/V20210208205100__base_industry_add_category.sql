ALTER TABLE `base_industry` 
ADD COLUMN `category` VARCHAR(45) NULL AFTER `name`;


INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `preset`, `deleted`, `create_by`, `update_by`, `tenant_id`) 
VALUES ('4387233486537228500', '行业分类', 'industry', '1', '0', '1','1', '0');
INSERT INTO `sys_dict_data` (`id`, `dict_code`,`dict_id`, `dict_show_name`, `dict_value`, `deleted`, `create_by`, `update_by`)
VALUES ('4387233486537228501', 'industry', '4387233486537228500', '互联网', '1', '0', '0', '0');
INSERT INTO `sys_dict_data` (`id`, `dict_code`,`dict_id`, `dict_show_name`, `dict_value`, `deleted`, `create_by`, `update_by`)
VALUES ('4387233486537228503', 'industry', '4387233486537228500', '金融', '2', '0', '0', '0');
INSERT INTO `sys_dict_data` (`id`, `dict_code`,`dict_id`, `dict_show_name`, `dict_value`, `deleted`, `create_by`, `update_by`)
VALUES ('4387233486537228504', 'industry', '4387233486537228500', '商务', '3', '0', '0', '0');
INSERT INTO `sys_dict_data` (`id`, `dict_code`,`dict_id`, `dict_show_name`, `dict_value`, `deleted`, `create_by`, `update_by`)
VALUES ('4387233486537228505', 'industry', '4387233486537228500', '医疗', '4', '0', '0', '0');
INSERT INTO `sys_dict_data` (`id`, `dict_code`,`dict_id`, `dict_show_name`, `dict_value`, `deleted`, `create_by`, `update_by`)
VALUES ('4387233486537228506', 'industry', '4387233486537228500', '传媒', '5', '0', '0', '0');
INSERT INTO `sys_dict_data` (`id`, `dict_code`,`dict_id`, `dict_show_name`, `dict_value`, `deleted`, `create_by`, `update_by`)
VALUES ('4387233486537228507', 'industry', '4387233486537228500', '教育', '6', '0', '0', '0');
INSERT INTO `sys_dict_data` (`id`, `dict_code`,`dict_id`, `dict_show_name`, `dict_value`, `deleted`, `create_by`, `update_by`)
VALUES ('4387233486537228508', 'industry', '4387233486537228500', '制造', '7', '0', '0', '0');
INSERT INTO `sys_dict_data` (`id`, `dict_code`,`dict_id`, `dict_show_name`, `dict_value`, `deleted`, `create_by`, `update_by`)
VALUES ('4387233486537228509', 'industry', '4387233486537228500', '地产', '8', '0', '0', '0');
INSERT INTO `sys_dict_data` (`id`, `dict_code`,`dict_id`, `dict_show_name`, `dict_value`, `deleted`, `create_by`, `update_by`)
VALUES ('4387233486537228510', 'industry', '4387233486537228500', '文娱', '9', '0', '0', '0');
INSERT INTO `sys_dict_data` (`id`, `dict_code`,`dict_id`, `dict_show_name`, `dict_value`, `deleted`, `create_by`, `update_by`)
VALUES ('4387233486537228511', 'industry', '4387233486537228500', '公共', '10', '0', '0', '0');
