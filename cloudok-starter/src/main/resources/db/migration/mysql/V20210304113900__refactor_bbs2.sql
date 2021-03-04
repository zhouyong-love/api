ALTER TABLE `bbs_post` 
CHANGE COLUMN `tag_type` `topic_type` INT(11) NULL DEFAULT NULL COMMENT '话题类型 0 系统推荐 1 研究领域 2 行业 3 社团 4 个性标签 5 状态标签 6 学校 7 专业' ,
CHANGE COLUMN `tag_id` `topic_id` BIGINT(20) NULL DEFAULT NULL COMMENT '话题id' ;

ALTER TABLE  `bbs_post` 
ADD COLUMN `topic_icon` VARCHAR(45) NULL AFTER `attach_ids`,
ADD COLUMN `topic_name` VARCHAR(255) NULL AFTER `topic_icon`;
