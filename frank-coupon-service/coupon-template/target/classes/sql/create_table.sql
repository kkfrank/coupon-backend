CREATE TABLE IF NOT EXISTS `coupon_db`.`coupon_template`(
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增组件',
  `available` boolean NOT NULL DEFAULT false COMMENT '是否可用',
  `expired` boolean NOT NULL DEFAULT false COMMENT '是否过期',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT 'coupon name',
  `logo` varchar(256) NOT NULL DEFAULT '' COMMENT '优惠券logo',
  `desc` varchar(256) NOT NULL DEFAULT '' COMMENT '优惠券desc',

  `category` varchar(64) NOT NULL DEFAULT '' COMMENT '优惠券category',

  `product_line` int(11) NOT NULL DEFAULT '0' COMMENT '优惠券product_line',
  `count` int(11) NOT NULL DEFAULT '0' COMMENT '优惠券count',
  `create_time` datetime NOT NULL DEFAULT '0000-01-01' COMMENT '优惠券create_time',
  `create_by` bigint(20) NOT NULL DEFAULT '0' COMMENT '优惠券create_by',

  `template_key` varchar(128) NOT NULL DEFAULT '' COMMENT '优惠券template_key',
  `target` int(11) NOT NULL DEFAULT '0' COMMENT '优惠券target',
  `rule` varchar(1024) NOT NULL DEFAULT '' COMMENT '优惠券rule',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_create_by` (`create_by`),
  UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT = 'coupon template'

-- clear
truncate coupon_template