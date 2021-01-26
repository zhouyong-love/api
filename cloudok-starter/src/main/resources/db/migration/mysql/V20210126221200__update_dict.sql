ALTER TABLE `sys_dict_data` 
ADD COLUMN `dict_code` VARCHAR(64) NULL AFTER `dict_id`;

update sys_dict_data set dict_code = 'specialism' where dict_id=4387233486537228289;
update sys_dict_data set dict_code = 'degree' where dict_id=4387233486537228292;