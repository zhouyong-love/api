
-- -----------------------------------------------------
-- Table `base_topic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_topic` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `topic_id` BIGINT(20) NULL,
  `topic_type` INT NULL COMMENT '0 动态标签 1 研究领域 2 行业 3 社团 4 个性 5状态标签 6 学校 7 专业',
  `topic_name` VARCHAR(255) NULL,
  `topic_icon` VARCHAR(45) NULL,
  `post_count` INT NULL DEFAULT 0,
  `peer_count` INT NULL DEFAULT 0,
  `last_update_ts` TIMESTAMP NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '主题表，汇总peer与动态时间';


-- -----------------------------------------------------
-- Table `uc_member_topic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uc_member_topic` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `member_id` BIGINT(20) NOT NULL COMMENT '用户id',
  `topic_id` BIGINT(20) NOT NULL,
  `last_post_id` BIGINT(20) NULL COMMENT '读的最新一条post',
  PRIMARY KEY (`id`),
  INDEX `fk_uc_firends_uc_member2_idx` (`member_id` ASC),
  INDEX `fk_uc_notification_copy1_base_topic1_idx` (`topic_id` ASC),
  CONSTRAINT `fk_uc_firends_uc_member210`
    FOREIGN KEY (`member_id`)
    REFERENCES `uc_member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_uc_notification_copy1_base_topic1`
    FOREIGN KEY (`topic_id`)
    REFERENCES `base_topic` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '用户与主题表关系';