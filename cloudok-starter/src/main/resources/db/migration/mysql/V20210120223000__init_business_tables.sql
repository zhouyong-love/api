
-- -----------------------------------------------------
-- Table `base_table`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_table` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '基础数据表';


-- -----------------------------------------------------
-- Table `uc_member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uc_member` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `user_name` VARCHAR(255) NOT NULL COMMENT '用户名',
  `email` VARCHAR(255) NOT NULL COMMENT '邮箱',
  `password` VARCHAR(45) NULL COMMENT '密码',
  `nick_name` VARCHAR(45) NULL COMMENT '昵称',
  `real_name` VARCHAR(45) NULL COMMENT '真实姓名（保留暂时不使用）',
  `birth_date` DATE NULL COMMENT '出生日期（保留）',
  `sex` VARCHAR(10) NULL COMMENT '性别字典（数据来源字典）',
  `phone` VARCHAR(15) NULL COMMENT '电话号码 保留字段',
  `avatar` BIGINT(20) NULL COMMENT '头像id（关联附件表id）',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '会员表';


-- -----------------------------------------------------
-- Table `base_school`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_school` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `name` VARCHAR(255) NULL COMMENT '学校名称',
  `email_postfix` VARCHAR(100) NULL COMMENT '邮箱后缀',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '学校基础数据';


-- -----------------------------------------------------
-- Table `base_specialism`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_specialism` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `name` VARCHAR(255) NULL COMMENT '专业',
  `category` VARCHAR(45) NULL COMMENT '大类：经管学部，信息等来源字典',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '专业   增加 文理分科';


-- -----------------------------------------------------
-- Table `uc_education_experience`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uc_education_experience` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `member_id` BIGINT(20) NOT NULL,
  `school_id` BIGINT(20) NOT NULL,
  `specialism_id` BIGINT(20) NOT NULL,
  `grade` INT NULL COMMENT '年级-只选 (2020-2010)',
  `degree` VARCHAR(20) NULL COMMENT '学位：来源字典',
  PRIMARY KEY (`id`),
  INDEX `fk_uc_education_experience_uc_member1_idx` (`member_id` ASC),
  INDEX `fk_uc_education_experience_base_school1_idx` (`school_id` ASC),
  INDEX `fk_uc_education_experience_base_specialities1_idx` (`specialism_id` ASC),
  CONSTRAINT `fk_uc_education_experience_uc_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `uc_member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_uc_education_experience_base_school1`
    FOREIGN KEY (`school_id`)
    REFERENCES `base_school` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_uc_education_experience_base_specialities1`
    FOREIGN KEY (`specialism_id`)
    REFERENCES `base_specialism` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '教育经历 学历： 本，硕士，博士';


-- -----------------------------------------------------
-- Table `base_school_specialities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_school_specialities` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `school_id` BIGINT(20) NOT NULL,
  `specialism_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_base_school_specialities_base_school1_idx` (`school_id` ASC),
  INDEX `fk_base_school_specialities_base_specialities1_idx` (`specialism_id` ASC),
  CONSTRAINT `fk_base_school_specialities_base_school1`
    FOREIGN KEY (`school_id`)
    REFERENCES `base_school` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_base_school_specialities_base_specialities1`
    FOREIGN KEY (`specialism_id`)
    REFERENCES `base_specialism` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '学校专业关联';


-- -----------------------------------------------------
-- Table `base_company`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_company` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `name` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '公司信息';


-- -----------------------------------------------------
-- Table `base_job`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_job` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `name` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '岗位';


-- -----------------------------------------------------
-- Table `base_industry`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_industry` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `name` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '行业信息';


-- -----------------------------------------------------
-- Table `uc_internship_experience`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uc_internship_experience` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `member_id` BIGINT(20) NOT NULL,
  `company_id` BIGINT(20) NOT NULL,
  `job_id` BIGINT(20) NOT NULL,
  `industry_id` BIGINT(20) NOT NULL,
  `description` VARCHAR(1024) NULL COMMENT '自定义描述',
  PRIMARY KEY (`id`),
  INDEX `fk_uc_practice_experience_uc_member1_idx` (`member_id` ASC),
  INDEX `fk_uc_practice_experience_base_company1_idx` (`company_id` ASC),
  INDEX `fk_uc_practice_experience_base_job1_idx` (`job_id` ASC),
  INDEX `fk_uc_practice_experience_base_industry1_idx` (`industry_id` ASC),
  CONSTRAINT `fk_uc_practice_experience_uc_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `uc_member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_uc_practice_experience_base_company1`
    FOREIGN KEY (`company_id`)
    REFERENCES `base_company` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_uc_practice_experience_base_job1`
    FOREIGN KEY (`job_id`)
    REFERENCES `base_job` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_uc_practice_experience_base_industry1`
    FOREIGN KEY (`industry_id`)
    REFERENCES `base_industry` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '实习经历';


-- -----------------------------------------------------
-- Table `base_research_domain`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_research_domain` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `name` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '研究领域';


-- -----------------------------------------------------
-- Table `uc_research_experience`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uc_research_experience` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `member_id` BIGINT(20) NOT NULL,
  `domain_id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255) NULL COMMENT '项目简称',
  `description` VARCHAR(1024) NULL COMMENT '自定义描述',
  PRIMARY KEY (`id`),
  INDEX `fk_uc_research_experience_uc_member1_idx` (`member_id` ASC),
  INDEX `fk_uc_research_experience_base_research_domain1_idx` (`domain_id` ASC),
  CONSTRAINT `fk_uc_research_experience_uc_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `uc_member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_uc_research_experience_base_research_domain1`
    FOREIGN KEY (`domain_id`)
    REFERENCES `base_research_domain` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '科研经历';


-- -----------------------------------------------------
-- Table `base_association`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_association` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `name` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '社团数据';


-- -----------------------------------------------------
-- Table `uc_association_experience`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uc_association_experience` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `member_id` BIGINT(20) NOT NULL,
  `association_id` BIGINT(20) NOT NULL,
  `title` VARCHAR(20) NULL COMMENT '职务',
  `description` VARCHAR(1024) NULL COMMENT '自定义描述',
  PRIMARY KEY (`id`),
  INDEX `fk_uc_association_experience_uc_member1_idx` (`member_id` ASC),
  INDEX `fk_uc_association_experience_base_association1_idx` (`association_id` ASC),
  CONSTRAINT `fk_uc_association_experience_uc_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `uc_member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_uc_association_experience_base_association1`
    FOREIGN KEY (`association_id`)
    REFERENCES `base_association` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '社团经历';


-- -----------------------------------------------------
-- Table `uc_project_experience`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uc_project_experience` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `member_id` BIGINT(20) NOT NULL,
  `category` VARCHAR(20) NULL COMMENT '类别：来源于字典 ',
  `name` VARCHAR(255) NULL COMMENT '组织名称、项目名称',
  `job` VARCHAR(255) NULL COMMENT '职务',
  `description` VARCHAR(1024) NULL COMMENT '自定义描述',
  PRIMARY KEY (`id`),
  INDEX `fk_uc_project_experience_uc_member1_idx` (`member_id` ASC),
  CONSTRAINT `fk_uc_project_experience_uc_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `uc_member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '项目实践';


-- -----------------------------------------------------
-- Table `base_tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `base_tag` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `name` VARCHAR(45) NULL,
  `type` TINYINT(1) NULL COMMENT '类型： 1 系统标准  2 自定义标签',
  `category` VARCHAR(45) NULL COMMENT '标签分类 数据字典',
  `icon` BIGINT(20) NULL COMMENT '标签icon',
  `color` VARCHAR(45) NULL COMMENT '标签色彩',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '标签';


-- -----------------------------------------------------
-- Table `uc_member_tags`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uc_member_tags` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `type` TINYINT(1) NULL COMMENT '打标签的类型：  1 系统算法打的标签  2 用户自定义标签',
  `tag_id` BIGINT(20) NOT NULL,
  `member_id` BIGINT(20) NOT NULL,
  `weight` INT NULL COMMENT '标签权重（保留）',
  `description` VARCHAR(1024) NULL COMMENT '自定义描述',
  PRIMARY KEY (`id`),
  INDEX `fk_uc_member_tags_base_tag1_idx` (`tag_id` ASC),
  INDEX `fk_uc_member_tags_uc_member1_idx` (`member_id` ASC),
  CONSTRAINT `fk_uc_member_tags_base_tag1`
    FOREIGN KEY (`tag_id`)
    REFERENCES `base_tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_uc_member_tags_uc_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `uc_member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '用户标签';


-- -----------------------------------------------------
-- Table `uc_recognized`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `uc_recognized` (
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
  CONSTRAINT `fk_uc_firends_uc_member1`
    FOREIGN KEY (`source_id`)
    REFERENCES `uc_member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_uc_firends_uc_member2`
    FOREIGN KEY (`target_id`)
    REFERENCES `uc_member` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '认可某人';


-- -----------------------------------------------------
-- Table `sys_message`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sys_message` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `type` TINYINT(1) NULL COMMENT '类型： 系统消息，认可消息等',
  `from_id` BIGINT(20) NULL COMMENT '发送人id',
  `from_name` VARCHAR(255) NULL COMMENT '发送人名称',
  `to_id` BIGINT(20) NULL COMMENT '接收人id',
  `to_name` VARCHAR(255) NULL COMMENT '接收人名称',
  `content` VARCHAR(1024) NULL COMMENT '消息内容',
  `ext` VARCHAR(1024) NULL COMMENT '扩展信息',
  `status` TINYINT(1) NULL COMMENT '状态 0 未读  1 已读',
  `status_ts` TIMESTAMP NULL COMMENT '状态变更时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '消息';


-- -----------------------------------------------------
-- Table `bbs_post`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bbs_post` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `content` TEXT NULL COMMENT '消息内容',
  `thumbs_up_count` INT NULL DEFAULT 0,
  `reply_count` INT NULL DEFAULT 0,
  `collect_count` INT NULL DEFAULT 0 COMMENT '收藏数量',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '动态、帖子等';


-- -----------------------------------------------------
-- Table `bbs_topic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bbs_topic` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `name` VARCHAR(45) NULL COMMENT '话题名称',
  `icon` BIGINT(20) NULL COMMENT '话题icon',
  `status` TINYINT(1) NULL COMMENT '话题状态： 0 禁用 1 启用',
  `post_count` INT NULL DEFAULT 0 COMMENT '话题下 帖子数量（实时更新）',
  `type` TINYINT(1) NULL COMMENT '类型： 1 话题 2 。。',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '话题';


-- -----------------------------------------------------
-- Table `bbs_post_topic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bbs_post_topic` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `topic_id` BIGINT(20) NOT NULL,
  `post_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_bbs_post_topic_bbs_topic1_idx` (`topic_id` ASC),
  INDEX `fk_bbs_post_topic_bbs_post1_idx` (`post_id` ASC),
  CONSTRAINT `fk_bbs_post_topic_bbs_topic1`
    FOREIGN KEY (`topic_id`)
    REFERENCES `bbs_topic` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bbs_post_topic_bbs_post1`
    FOREIGN KEY (`post_id`)
    REFERENCES `bbs_post` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '动态，文章的话题关联';


-- -----------------------------------------------------
-- Table `bbs_comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bbs_comment` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `content` TEXT NULL COMMENT '消息内容',
  `thumbs_up_count` INT NULL DEFAULT 0,
  `reply_count` INT NULL DEFAULT 0,
  `collect_count` INT NULL DEFAULT 0 COMMENT '收藏数量',
  `post_id` BIGINT(20) NOT NULL,
  `path` VARCHAR(1024) NULL COMMENT '评论id路径： 从最顶层到当前这个 一级一级加。',
  `parent_id` BIGINT(20) NULL COMMENT '父级评论id',
  PRIMARY KEY (`id`),
  INDEX `fk_bbs_comment_bbs_post1_idx` (`post_id` ASC),
  CONSTRAINT `fk_bbs_comment_bbs_post1`
    FOREIGN KEY (`post_id`)
    REFERENCES `bbs_post` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '文章评论';
 
