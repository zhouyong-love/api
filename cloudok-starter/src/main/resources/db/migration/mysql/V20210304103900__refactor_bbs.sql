drop table bbs_post_topic;
drop table bbs_topic;
drop table bbs_comment; 
drop table bbs_collect;
drop table bbs_thumbs_up;
drop table bbs_post;
 

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
  `comment_count` INT NULL DEFAULT 0,
  `collect_count` INT NULL DEFAULT 0 COMMENT '收藏数量',
  `tag_type` INT NULL COMMENT '标签类型',
  `tag_id` BIGINT(20) NULL COMMENT '标签id',
  `attach_ids` VARCHAR(1024) NULL COMMENT '图片附件id',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '动态、帖子等';


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
COMMENT = '动态评论';


-- -----------------------------------------------------
-- Table `bbs_collect`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bbs_collect` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `business_type` TINYINT(1) NULL COMMENT '业务类型： 1 动态 2 评论',
  `business_id` BIGINT(20) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '收藏';


-- -----------------------------------------------------
-- Table `bbs_thumbs_up`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `bbs_thumbs_up` (
  `id` BIGINT(20) NOT NULL COMMENT '主键',
  `tenant_id` BIGINT(20) NULL DEFAULT 1 COMMENT '租户id',
  `deleted` TINYINT(1) NULL DEFAULT 0 COMMENT '删除状态： 0 未删除 1 已经删除',
  `create_by` BIGINT(20) NULL COMMENT '创建人id',
  `update_by` BIGINT(20) NULL COMMENT '修改人id',
  `create_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间戳',
  `update_ts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间戳',
  `business_type` TINYINT(1) NULL COMMENT '业务类型： 1 动态 2 评论',
  `business_id` BIGINT(20) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '点赞';
 
