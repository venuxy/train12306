DROP TABLE IF EXISTS `member`;

CREATE TABLE `member` (
                          `id` BIGINT NOT NULL COMMENT 'id',
                          `mobile` VARCHAR(11) NOT NULL COMMENT '手机号',
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `mobile_unique` (`mobile`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '会员';


INSERT INTO `member` (`id`, `mobile`) VALUES (1, '13800000000');
