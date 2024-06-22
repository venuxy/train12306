DROP TABLE IF EXISTS `member`;

CREATE TABLE `member` (
                          `id` BIGINT NOT NULL COMMENT 'id',
                          `mobile` VARCHAR(11) NOT NULL COMMENT '手机号',
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `mobile_unique` (`mobile`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '会员';


INSERT INTO `member` (`id`, `mobile`) VALUES (1, '13800000000');

drop table if exists `passenger`;
create table `passenger` (
                             `id` bigint not null comment 'id',
                             `member_id` bigint not null comment '会员id',
                             `name` varchar(20) not null comment '姓名',
                             `id_card` varchar(18) not null comment '身份证',
                             `type` char(1) not null comment '旅客类型|枚举[PassengerTypeEnum]',
                             `create_time` datetime(3) comment '新增时间',
                             `update_time` datetime(3) comment '修改时间',
                             primary key (`id`),
                             index `member_id_index` (`member_id`)
) engine=innodb default charset=utf8mb4 comment='乘车人';