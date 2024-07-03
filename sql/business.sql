drop table if exists `station`;
create table `station` (
                           `id` bigint not null comment 'id',
                           `name` varchar(20) not null comment '站名',
                           `name_pinyin` varchar(50) not null comment '站名拼音',
                           `name_py` varchar(50) not null comment '站名拼音首字母',
                           `create_time` datetime(3) comment '新增时间',
                           `update_time` datetime(3) comment '修改时间',
                           primary key (`id`),
                           unique key `name_unique` (`name`)
) engine=innodb default charset=utf8mb4 comment='车站';


drop table if exists `train_station`;
create table `train_station` (
                                 `id` bigint not null comment 'id',
                                 `train_code` varchar(20) not null comment '车次编号',
                                 `index` int not null comment '站序',
                                 `name` varchar(20) not null comment '站名',
                                 `name_pinyin` varchar(50) not null comment '站名拼音',
                                 `in_time` time comment '进站时间',
                                 `out_time` time comment '出站时间',
                                 `stop_time` time comment '停站时长',
                                 `km` decimal(8, 2) not null comment '里程（公里）|从上一站到本站的距离',
                                 `create_time` datetime(3) comment '新增时间',
                                 `update_time` datetime(3) comment '修改时间',
                                 primary key (`id`),
                                 unique key `train_code_index_unique` (`train_code`, `index`),
                                 unique key `train_code_name_unique` (`train_code`, `name`)
) engine=innodb default charset=utf8mb4 comment='火车车站';

drop table if exists `train_carriage`;
create table `train_carriage` (
                                  `id` bigint not null comment 'id',
                                  `train_code` varchar(20) not null comment '车次编号',
                                  `index` int not null comment '厢号',
                                  `seat_type` char(1) not null comment '座位类型|枚举[SeatTypeEnum]',
                                  `seat_count` int not null comment '座位数',
                                  `row_count` int not null comment '排数',
                                  `col_count` int not null comment '列数',
                                  `create_time` datetime(3) comment '新增时间',
                                  `update_time` datetime(3) comment '修改时间',
                                  unique key `train_code_index_unique` (`train_code`, `index`),
                                  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='火车车厢';

drop table if exists `train_seat`;
create table `train_seat` (
                              `id` bigint not null comment 'id',
                              `train_code` varchar(20) not null comment '车次编号',
                              `carriage_index` int not null comment '厢序',
                              `row` char(2) not null comment '排号|01, 02',
                              `col` char(1) not null comment '列号|枚举[SeatColEnum]',
                              `seat_type` char(1) not null comment '座位类型|枚举[SeatTypeEnum]',
                              `carriage_seat_index` int not null comment '同车厢座序',
                              `create_time` datetime(3) comment '新增时间',
                              `update_time` datetime(3) comment '修改时间',
                              primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='座位';

drop table if exists `daily_train`;
create table `daily_train` (
                               `id` bigint not null comment 'id',
                               `date` date not null comment '日期',
                               `code` varchar(20) not null comment '车次编号',
                               `type` char(1) not null comment '车次类型|枚举[TrainTypeEnum]',
                               `start` varchar(20) not null comment '始发站',
                               `start_pinyin` varchar(50) not null comment '始发站拼音',
                               `start_time` time not null comment '出发时间',
                               `end` varchar(20) not null comment '终点站',
                               `end_pinyin` varchar(50) not null comment '终点站拼音',
                               `end_time` time not null comment '到站时间',
                               `create_time` datetime(3) comment '新增时间',
                               `update_time` datetime(3) comment '修改时间',
                               primary key (`id`),
                               unique key `date_code_unique` (`date`, `code`)
) engine=innodb default charset=utf8mb4 comment='每日车次';

drop table if exists `daily_train_station`;
create table `daily_train_station` (
                                       `id` bigint not null comment 'id',
                                       `date` date not null comment '日期',
                                       `train_code` varchar(20) not null comment '车次编号',
                                       `index` int not null comment '站序|第一站是0',
                                       `name` varchar(20) not null comment '站名',
                                       `name_pinyin` varchar(50) not null comment '站名拼音',
                                       `in_time` time comment '进站时间',
                                       `out_time` time comment '出站时间',
                                       `stop_time` time comment '停站时长',
                                       `km` decimal(8, 2) not null comment '里程（公里）|从上一站到本站的距离',
                                       `create_time` datetime(3) comment '新增时间',
                                       `update_time` datetime(3) comment '修改时间',
                                       primary key (`id`),
                                       unique key `date_train_code_index_unique` (`date`, `train_code`, `index`),
                                       unique key `date_train_code_name_unique` (`date`, `train_code`, `name`)
) engine=innodb default charset=utf8mb4 comment='每日车站';

drop table if exists `daily_train_carriage`;
create table `daily_train_carriage` (
                                        `id` bigint not null comment 'id',
                                        `date` date not null comment '日期',
                                        `train_code` varchar(20) not null comment '车次编号',
                                        `index` int not null comment '箱序',
                                        `seat_type` char(1) not null comment '座位类型|枚举[SeatTypeEnum]',
                                        `seat_count` int not null comment '座位数',
                                        `row_count` int not null comment '排数',
                                        `col_count` int not null comment '列数',
                                        `create_time` datetime(3) comment '新增时间',
                                        `update_time` datetime(3) comment '修改时间',
                                        primary key (`id`),
                                        unique key `date_train_code_index_unique` (`date`, `train_code`, `index`)
) engine=innodb default charset=utf8mb4 comment='每日车厢';

drop table if exists `daily_train_seat`;
create table `daily_train_seat` (
                                    `id` bigint not null comment 'id',
                                    `date` date not null comment '日期',
                                    `train_code` varchar(20) not null comment '车次编号',
                                    `carriage_index` int not null comment '箱序',
                                    `row` char(2) not null comment '排号|01, 02',
                                    `col` char(1) not null comment '列号|枚举[SeatColEnum]',
                                    `seat_type` char(1) not null comment '座位类型|枚举[SeatTypeEnum]',
                                    `carriage_seat_index` int not null comment '同车箱座序',
                                    `sell` varchar(50) not null comment '售卖情况|将经过的车站用01拼接，0表示可卖，1表示已卖',
                                    `create_time` datetime(3) comment '新增时间',
                                    `update_time` datetime(3) comment '修改时间',
                                    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='每日座位';
