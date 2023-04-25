CREATE TABLE `action_variable` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `action_id` bigint(20) DEFAULT '0',
                                   `vid` bigint(20) NOT NULL DEFAULT '0' COMMENT '变量id',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uniq_one` (`action_id`,`vid`),
                                   KEY `idx_vid` (`vid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `common_config` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `group` int(11) NOT NULL DEFAULT '0',
                                 `key` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                 `value` varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                                 `sort` int(11) NOT NULL DEFAULT '0',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_key` (`group`,`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `common_text` (
                               `Id` bigint(20) NOT NULL AUTO_INCREMENT,
                               `fid` bigint(20) NOT NULL DEFAULT '0' COMMENT '关联表主键',
                               `text` varchar(5000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '文本',
                               `type` int(11) NOT NULL DEFAULT '0' COMMENT '文本类型',
                               PRIMARY KEY (`Id`),
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
                                  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
                                  `fid` bigint(20) NOT NULL DEFAULT '0' COMMENT '关联表主键',
                                  `text` varchar(5000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '文本',
                                  `type` int(11) NOT NULL DEFAULT '0' COMMENT '文本类型',
                                  PRIMARY KEY (`Id`),
                                  KEY `idx_fid` (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='大字段存储';

CREATE TABLE `data_source` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT,
                               `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                               `type` int(11) NOT NULL DEFAULT '0' COMMENT '类型 1数据库 2 dubbo',
                               `version` int(11) NOT NULL DEFAULT '0',
                               `is_active` tinyint(1) NOT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据源';

CREATE TABLE `variable` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT ' ',
                            `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '变量名',
                            `cid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'component id 组件id',
                            `c_res_path` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'c+cid+_+name 组件结果集name',
                            `type` int(11) NOT NULL DEFAULT '0',
                            `type_path` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '类型：java类路径',
                            `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
                            `domain` int(11) NOT NULL DEFAULT '0',
                            `is_del` tinyint(1) NOT NULL DEFAULT '0',
                            `is_active` tinyint(1) NOT NULL DEFAULT '1',
                            `create_time` datetime DEFAULT NULL,
                            `update_time` datetime DEFAULT NULL,
                            `busi_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

