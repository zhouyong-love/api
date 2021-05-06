ALTER TABLE `base_school`
ADD COLUMN `area_id`  BIGINT(20) NULL COMMENT '地区ID'  AFTER `sn`;

update base_school set area_id='4397233486537228900' where name='北京大学';
update base_school set area_id='4397233486537228900' where name='清华大学';
update base_school set area_id='4397233486537228900' where name='上海交通大学';
update base_school set area_id='4397233486537228900' where name='复旦大学';
update base_school set area_id='4397233486537228900' where name='中国人民大学';
update base_school set area_id='4397233486537228900' where name='浙江大学';
update base_school set area_id='4397233486537228900' where name='香港大学';