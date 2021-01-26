ALTER TABLE `base_school_specialities` 
DROP FOREIGN KEY `fk_base_school_specialities_base_specialities1`,
DROP FOREIGN KEY `fk_base_school_specialities_base_school1`;
ALTER TABLE `base_school_specialities` 
DROP INDEX `fk_base_school_specialities_base_specialities1_idx` ,
DROP INDEX `fk_base_school_specialities_base_school1_idx` ;
;
