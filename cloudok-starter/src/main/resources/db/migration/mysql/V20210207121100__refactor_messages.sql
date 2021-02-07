
-- -----------------------------------------------------
-- Table `uc_message_thread`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uc_message_thread` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `type` TINYINT(1) NULL COMMENT '类型： 1 留言 2 私信',
  `ower_id` BIGINT(20) NULL COMMENT '归属id，对于私信 不存在归属于谁，对于留言 留给谁的就是谁的',
  `is_public` TINYINT(1) NULL COMMENT '是否公开：默认为false，只有参与人可见',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '消息对话组（包含私信，留言等）';


-- -----------------------------------------------------
-- Table `uc_message`
-- -----------------------------------------------------
drop table uc_message;
CREATE TABLE IF NOT EXISTS `uc_message` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `type` TINYINT(1) NULL COMMENT '类型： 1 认可消息 2 普通消息',
  `content` VARCHAR(1024) NULL COMMENT '消息内容',
  `thread_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_uc_message_uc_message_thread1_idx` (`thread_id` ASC),
  CONSTRAINT `fk_uc_message_uc_message_thread1`
    FOREIGN KEY (`thread_id`)
    REFERENCES `uc_message_thread` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '消息';


-- -----------------------------------------------------
-- Table `uc_firend`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uc_firend` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `source_id` BIGINT(20) NOT NULL COMMENT '认可发起方 ',
  `target_id` BIGINT(20) NOT NULL COMMENT '被认可方',
  PRIMARY KEY (`id`),
  INDEX `fk_uc_firends_uc_member1_idx` (`source_id` ASC),
  INDEX `fk_uc_firends_uc_member2_idx` (`target_id` ASC),
  CONSTRAINT `fk_uc_firends_uc_member10`
    FOREIGN KEY (`source_id`)
    REFERENCES `uc_member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_uc_firends_uc_member20`
    FOREIGN KEY (`target_id`)
    REFERENCES `uc_member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '好友表';


-- -----------------------------------------------------
-- Table `uc_message_thread_members`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uc_message_thread_members` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `thread_id` BIGINT(20) NOT NULL,
  `last_position` BIGINT(20) NULL COMMENT '已读消息最后位置',
  PRIMARY KEY (`id`),
  INDEX `fk_uc_message_uc_message_thread1_idx` (`thread_id` ASC),
  CONSTRAINT `fk_uc_message_uc_message_thread10`
    FOREIGN KEY (`thread_id`)
    REFERENCES `uc_message_thread` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '消息参与方';
