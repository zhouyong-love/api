CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL,
  `user_name` varchar(64) NOT NULL COMMENT '登录名',
  `user_full_name` varchar(64) DEFAULT NULL COMMENT '用户姓名',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `sex` varchar(8) DEFAULT NULL COMMENT '性别',
  `birth_day` date DEFAULT NULL COMMENT '生日',
  `telphone` varchar(45) DEFAULT NULL COMMENT '电话',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `freeze` tinyint(1) DEFAULT NULL COMMENT '是否冻结',
  `avatar` bigint(20) DEFAULT NULL COMMENT '用户头像',
  `last_login_time` timestamp NULL DEFAULT NULL,
  `last_login_addr` varchar(64) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_ts` timestamp NULL DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';


CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL,
  `role_code` varchar(64) NOT NULL COMMENT '角色代码',
  `role_name` varchar(128) NOT NULL COMMENT '角色名称',
  `remark` varchar(512) DEFAULT NULL COMMENT '描述',
  `role_type` varchar(64) NOT NULL COMMENT '角色类型',
  `deleted` tinyint(1) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_ts` timestamp NULL DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

CREATE TABLE `sys_resource` (
  `id` bigint(20) NOT NULL,
  `resource_code` varchar(64) NOT NULL COMMENT '资源编码',
  `resource_name` varchar(128) NOT NULL COMMENT '资源名称',
  `parent_id` bigint(20) NOT NULL COMMENT '上级资源',
  `resource_url` varchar(128) DEFAULT NULL COMMENT '资源地址',
  `resource_icon` varchar(128) DEFAULT NULL COMMENT '资源图标',
  `resource_path` varchar(128) DEFAULT NULL COMMENT '资源文件',
  `resource_type` varchar(24) NOT NULL COMMENT '资源类型',
  `remark` varchar(512) DEFAULT NULL COMMENT '描述',
  `sn` bigint(20) DEFAULT NULL COMMENT '排序',
  `deleted` tinyint(1) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_ts` timestamp NULL DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源表';

CREATE TABLE `sys_role_res` (
  `id` bigint(20) NOT NULL,
  `res_id` bigint(20) NOT NULL COMMENT '资源',
  `role_id` bigint(20) NOT NULL COMMENT '角色',
  `deleted` tinyint(1) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_ts` timestamp NULL DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源与角色关系';

CREATE TABLE `sys_obj_role` (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL COMMENT '角色',
  `obj_id` bigint(20) NOT NULL COMMENT '角色载体',
  `obj_type` varchar(24) NOT NULL COMMENT '载体类型',
  `deleted` tinyint(1) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_ts` timestamp NULL DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='载体角色关系表';

CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL,
  `dict_name` varchar(128) NOT NULL COMMENT '字典名称',
  `dict_code` varchar(64) DEFAULT NULL COMMENT '字典编码',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `preset` tinyint(1) NOT NULL COMMENT '预设值，字典不允许修改',
  `deleted` tinyint(1) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_ts` timestamp NULL DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dict_code_UNIQUE` (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典类型';

CREATE TABLE `sys_dict_data` (
  `id` bigint(20) NOT NULL,
  `dict_id` bigint(20) NOT NULL COMMENT '字典',
  `dict_show_name` varchar(128) DEFAULT NULL COMMENT '字典显示名称',
  `dict_value` varchar(64) DEFAULT NULL COMMENT '字典值',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `sn` bigint(20) DEFAULT NULL COMMENT '排序',
  `deleted` tinyint(1) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_ts` timestamp NULL DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典值';

CREATE TABLE `sys_attach` (
  `id` bigint(20) NOT NULL,
  `storage_model` varchar(24) NOT NULL COMMENT '存储模式',
  `address` varchar(256) DEFAULT NULL COMMENT '附件地址，本地地址或其他地址',
  `business_id` bigint(20) DEFAULT NULL COMMENT '业务主键',
  `business_key` varchar(64) DEFAULT NULL COMMENT '所属模块',
  `used` tinyint(1) DEFAULT NULL COMMENT '是否使用',
  `file_name` varchar(128) DEFAULT NULL COMMENT '文件名',
  `suffix` varchar(64) DEFAULT NULL COMMENT '后缀名',
  `file_size` bigint(10) DEFAULT NULL COMMENT '附件大小',
  `file_type` varchar(64) DEFAULT NULL COMMENT '附件类型',
  `business_remark` varchar(512) DEFAULT NULL COMMENT '业务备注',
  `deleted` tinyint(1) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_ts` timestamp NULL DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  `update_ts` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='附件信息表';
