ALTER TABLE `uc_association_experience` 
ADD COLUMN `sn` INT NULL DEFAULT 0 AFTER `description`;

ALTER TABLE `uc_education_experience` 
ADD COLUMN `sn` INT NULL DEFAULT 0 AFTER `grade`;

ALTER TABLE `uc_internship_experience` 
ADD COLUMN `sn` INT NULL DEFAULT 0 AFTER `description`;

ALTER TABLE `uc_project_experience` 
ADD COLUMN `sn` INT NULL DEFAULT 0 AFTER `description`;

ALTER TABLE `uc_research_experience` 
ADD COLUMN `sn` INT NULL DEFAULT 0 AFTER `description`;