ALTER TABLE `base_school` 
ADD COLUMN `abbreviation` VARCHAR(45) NULL AFTER `email_postfix`;

update base_school set abbreviation='PKU' where name='北京大学';
update base_school set abbreviation='TSINGHUA' where name='清华大学';