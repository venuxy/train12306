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

