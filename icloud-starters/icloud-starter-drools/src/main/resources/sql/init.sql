CREATE TABLE `action` (
                          `id` bigint(20) DEFAULT NULL,
                          `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                          `cid` bigint(20) NOT NULL DEFAULT '0' COMMENT '组件id',
                          `version` int(11) NOT NULL DEFAULT '0',
                          `is_active` tinyint(1) NOT NULL DEFAULT '0',
                          `is_del` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='动作';

CREATE TABLE `action_log` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `core_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '限界id',
                              `run_log_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'log id',
                              `cid` bigint(20) NOT NULL DEFAULT '0' COMMENT '组件id',
                              `status` int(11) NOT NULL DEFAULT '0',
                              `fail_num` int(11) NOT NULL DEFAULT '0',
                              `fail_reason` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                              `version` int(11) NOT NULL DEFAULT '0',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `common_config` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `group` varchar(255) NOT NULL DEFAULT '',
                                 `key` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                 `value` varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                                 `sort` int(11) NOT NULL DEFAULT '0',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_key` (`group`,`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `common_text` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                               `fid` bigint(20) NOT NULL DEFAULT '0' COMMENT '关联表主键',
                               `text` varchar(5000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '文本',
                               `type` int(11) NOT NULL DEFAULT '0' COMMENT '文本类型',
                               PRIMARY KEY (`id`),
                               KEY `idx_fid` (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='大字段存储';

CREATE TABLE `component` (
                             `id` bigint(20) DEFAULT NULL,
                             `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                             `data_source_id` bigint(20) NOT NULL DEFAULT '0',
                             `data_source_type` int(11) NOT NULL DEFAULT '0',
                             `version` int(11) NOT NULL DEFAULT '0',
                             `is_active` tinyint(1) NOT NULL DEFAULT '0',
                             `is_del` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组件';

CREATE TABLE `component_text` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                  `fid` bigint(20) NOT NULL DEFAULT '0' COMMENT '关联表主键',
                                  `text` varchar(5000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '文本',
                                  `type` int(11) NOT NULL DEFAULT '0' COMMENT '文本类型',
                                  PRIMARY KEY (`id`),
                                  KEY `idx_fid` (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='大字段存储';

CREATE TABLE `data_source` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                               `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                               `type` int(11) NOT NULL DEFAULT '0' COMMENT '类型 1数据库 2 dubbo',
                               `version` int(11) NOT NULL DEFAULT '0',
                               `is_active` tinyint(1) NOT NULL DEFAULT '0',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据源';

CREATE TABLE `fixed_num_allotter_log` (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                          `uid` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '',
                                          `busi_date` date NOT NULL,
                                          `core_id` bigint(20) NOT NULL DEFAULT '0',
                                          `rid` bigint(20) NOT NULL DEFAULT '0' COMMENT '规则模版id',
                                          `target_id` bigint(20) NOT NULL DEFAULT '0',
                                          `ref_total` decimal(20,2) NOT NULL DEFAULT '0.00',
                                          `num` bigint(20) NOT NULL DEFAULT '0',
                                          PRIMARY KEY (`id`),
                                          UNIQUE KEY `uniq_one` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `ratio_allotter_log` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                      `uid` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '',
                                      `busi_date` date NOT NULL,
                                      `core_id` bigint(20) NOT NULL DEFAULT '0',
                                      `rid` bigint(20) NOT NULL DEFAULT '0' COMMENT '规则模版id',
                                      `target_id` bigint(20) NOT NULL DEFAULT '0',
                                      `ref_total` decimal(20,2) NOT NULL DEFAULT '0.00',
                                      `num` bigint(20) NOT NULL DEFAULT '0',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uniq_one` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `rule_core` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT,
                             `domain` int(11) NOT NULL DEFAULT '0' COMMENT '域',
                             `action_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '动作',
                             `busi_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '业务编码',
                             `version` bigint(20) NOT NULL DEFAULT '0',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `uniq_one` (`domain`,`action_id`,`busi_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='规则';

CREATE TABLE `rule_template` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `domain` int(11) NOT NULL DEFAULT '0' COMMENT '域',
                                 `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '规则模版名称',
                                 `comment` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '描述',
                                 `action_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '动作',
                                 `cron` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'cron时间表达式',
                                 `allocation_model` int(11) NOT NULL DEFAULT '0' COMMENT '分配算法:1固定数量2比例',
                                 `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0未启用 1启用',
                                 `version` int(11) NOT NULL DEFAULT '0',
                                 `org_id` bigint(20) NOT NULL DEFAULT '0',
                                 `create_time` datetime DEFAULT NULL,
                                 `update_time` datetime DEFAULT NULL,
                                 `is_del` tinyint(1) NOT NULL DEFAULT '0',
                                 `ref` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '蛇形算法参照物',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='规则模版';

CREATE TABLE `rule_template_busi` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                      `tid` bigint(20) NOT NULL DEFAULT '0' COMMENT '模版id',
                                      `busi_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '业务编码',
                                      `version` int(11) NOT NULL DEFAULT '0',
                                      `busi_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                                      PRIMARY KEY (`id`),
                                      KEY `idx_busi` (`busi_code`),
                                      KEY `idx_tid` (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='规则模版-关联业务';

CREATE TABLE `run_core_text` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `fid` bigint(20) NOT NULL DEFAULT '0' COMMENT '关联表主键',
                                 `text` varchar(5000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '文本',
                                 `type` int(11) NOT NULL DEFAULT '0' COMMENT '文本类型',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_fid` (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='大字段存储';

CREATE TABLE `run_log` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `core_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '限界id',
                           `busi_date` date NOT NULL,
                           `source_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '源id:外部业务标识',
                           `target_id` bigint(20) NOT NULL DEFAULT '0',
                           `tid` bigint(20) NOT NULL DEFAULT '0' COMMENT '规则模版id',
                           `lock_status` tinyint(4) NOT NULL DEFAULT '0',
                           `lock_version` bigint(20) NOT NULL DEFAULT '0',
                           `lock_time` datetime DEFAULT NULL,
                           `status` int(11) NOT NULL DEFAULT '0',
                           `sub_status` int(11) NOT NULL DEFAULT '0',
                           `sub_fail_num` int(11) NOT NULL DEFAULT '0',
                           `sub_fail_reason` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                           `version` int(11) NOT NULL DEFAULT '0',
                           `action_id` bigint(20) NOT NULL DEFAULT '0',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `uniq_one` (`busi_date`,`core_id`,`source_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `run_log_text` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                `type` int(11) NOT NULL DEFAULT '0',
                                `text` varchar(5000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                                `fid` bigint(20) NOT NULL DEFAULT '0',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='日志大文本';

