CREATE TABLE `base_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL COMMENT '多租户id(留用)',
  `message_type` varchar(40) NOT NULL COMMENT '消息类型，如报警、告警、注册短信',
  `message_content` text COMMENT '发送内容，如果是走模版的，则先解析生成也存到此字段中',
  `title` varchar(60) DEFAULT NULL COMMENT '消息标题',
  `status` tinyint(4) DEFAULT NULL,
  `params` varchar(2000) DEFAULT NULL COMMENT '消息参数，内容格式为json',
  `deleted` tinyint(1) NOT NULL COMMENT '逻辑删除字段，0未删除, 1已删除',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `user_name` varchar(30) DEFAULT NULL,
  `create_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '最后更新者主键',
  `update_ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息表';


CREATE TABLE `base_message_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint(20) NOT NULL COMMENT '多租户id(留用)',
  `message_id` bigint(20) NOT NULL COMMENT '消息主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id ,可选，有用户id时就保存',
  `user_name` varchar(30) DEFAULT NULL,
  `receiver` varchar(60) DEFAULT NULL COMMENT '接收对象，依message_type而定，支持的有短信 email app信息等',
  `receiver_type` varchar(40) NOT NULL COMMENT '接收者 类型',
  `resend_count` int(11) DEFAULT NULL COMMENT '重发次数',
  `status` tinyint(4) NOT NULL COMMENT '0 新状态，1已发送，2发送失败， 99 已送达',
  `call_message` text COMMENT '调用第三主发送服务 返回的response',
  `deleted` tinyint(1) NOT NULL COMMENT '逻辑删除字段，0未删除, 1已删除',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者主键',
  `create_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '最后更新者主键',
  `update_ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `fk_message_details_message1_idx` (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息明细表\n已细化到接收人';