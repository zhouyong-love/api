ALTER TABLE `cloudok`.`uc_member` 
ADD COLUMN `state` BIGINT(20) NULL COMMENT '用户状态  bit 0  是否设置了用户信息 bit 1 是否设置了教育信息   bit 2 是否验证了邮箱 其他保留' AFTER `avatar`,
CHANGE COLUMN `user_name` `user_name` VARCHAR(255) NULL COMMENT '用户名' ,
CHANGE COLUMN `email` `email` VARCHAR(255) NULL COMMENT '邮箱' ;

