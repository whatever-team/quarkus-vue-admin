
-- BEGIN TABLE sys_dept
DROP TABLE IF EXISTS sys_dept;
CREATE TABLE `sys_dept`
(
    `id`             bigint(20) NOT NULL,
    `createBy`       varchar(255) DEFAULT NULL,
    `createTime`     datetime(6)  DEFAULT NULL,
    `lastModifyBy`   varchar(255) DEFAULT NULL,
    `lastModifyTime` datetime(6)  DEFAULT NULL,
    `remark`         varchar(255) DEFAULT NULL,
    `seq`            bigint(20)   DEFAULT NULL,
    `contact`        varchar(255) DEFAULT NULL,
    `deptName`       varchar(255) DEFAULT NULL,
    `enabled`        bit(1)       DEFAULT NULL,
    `leader`         varchar(255) DEFAULT NULL,
    `parentId`       bigint(20)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `index_dept_name` (`deptName`),
    KEY `index_parentId` (`parentId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Inserting 4 rows into sys_dept
-- Insert batch #1
INSERT INTO sys_dept (id, createBy, createTime, lastModifyBy, lastModifyTime, remark, seq, contact, deptName, enabled,
                      leader, parentId)
VALUES (1413756284733362176, 'default', '2021-07-10 15:05:39.259000', NULL, NULL, NULL, 0, '', '夸克', 1, '',
        0),
       (1413756387648999424, 'default', '2021-07-10 15:06:03.796000', NULL, NULL, NULL, 0, '', '技术部', 1, '',
        1413756284733362176),
       (1413756452069314560, 'default', '2021-07-10 15:06:19.155000', NULL, NULL, NULL, 0, '', '设计部', 1, '',
        1413756284733362176),
       (1413756526576930816, 'default', '2021-07-10 15:06:36.919000', NULL, NULL, NULL, 0, '', 'Java组', 1, '',
        1413756387648999424);

-- END TABLE sys_dept

-- BEGIN TABLE sys_dict
DROP TABLE IF EXISTS sys_dict;
CREATE TABLE `sys_dict`
(
    `id`             bigint(20) NOT NULL,
    `createBy`       varchar(255) DEFAULT NULL,
    `createTime`     datetime(6)  DEFAULT NULL,
    `lastModifyBy`   varchar(255) DEFAULT NULL,
    `lastModifyTime` datetime(6)  DEFAULT NULL,
    `remark`         varchar(255) DEFAULT NULL,
    `seq`            bigint(20)   DEFAULT NULL,
    `dictCode`       varchar(255) DEFAULT NULL,
    `dictName`       varchar(255) DEFAULT NULL,
    `enabled`        bit(1)       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `index_dict_code` (`dictCode`),
    KEY `index_enabled` (`enabled`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Inserting 1 row into sys_dict
-- Insert batch #1
INSERT INTO sys_dict (id, createBy, createTime, lastModifyBy, lastModifyTime, remark, seq, dictCode, dictName, enabled)
VALUES (1419837636738158592, 'default', '2021-07-27 09:50:46.558000', NULL, NULL, NULL, NULL, 'enabled', '启用状态', 1);

-- END TABLE sys_dict

-- BEGIN TABLE sys_dict_item
DROP TABLE IF EXISTS sys_dict_item;
CREATE TABLE `sys_dict_item`
(
    `id`             bigint(20) NOT NULL,
    `createBy`       varchar(255) DEFAULT NULL,
    `createTime`     datetime(6)  DEFAULT NULL,
    `lastModifyBy`   varchar(255) DEFAULT NULL,
    `lastModifyTime` datetime(6)  DEFAULT NULL,
    `remark`         varchar(255) DEFAULT NULL,
    `seq`            bigint(20)   DEFAULT NULL,
    `enabled`        bit(1)       DEFAULT NULL,
    `itemCode`       varchar(255) DEFAULT NULL,
    `itemName`       varchar(255) DEFAULT NULL,
    `dictCode`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `index_item_code` (`itemCode`),
    KEY `index_enabled` (`enabled`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Inserting 2 rows into sys_dict_item
-- Insert batch #1
INSERT INTO sys_dict_item (id, createBy, createTime, lastModifyBy, lastModifyTime, remark, seq, enabled, itemCode,
                           itemName, dictCode)
VALUES (1419837833182580736, 'default', '2021-07-27 09:51:33.399000', NULL, NULL, NULL, NULL, 1, 'true', '启用',
        'enabled'),
       (1419837865042513920, 'default', '2021-07-27 09:51:40.995000', NULL, NULL, NULL, NULL, 1, 'false', '关闭',
        'enabled');

-- END TABLE sys_dict_item

-- BEGIN TABLE sys_menu
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE `sys_menu`
(
    `id`             bigint(20) NOT NULL,
    `createBy`       varchar(255) DEFAULT NULL,
    `createTime`     datetime(6)  DEFAULT NULL,
    `lastModifyBy`   varchar(255) DEFAULT NULL,
    `lastModifyTime` datetime(6)  DEFAULT NULL,
    `remark`         varchar(255) DEFAULT NULL,
    `seq`            bigint(20)   DEFAULT NULL,
    `enabled`        bit(1)       DEFAULT NULL,
    `icon`           varchar(255) DEFAULT NULL,
    `menuName`       varchar(255) DEFAULT NULL,
    `parentId`       bigint(20)   DEFAULT NULL,
    `permission`     varchar(255) DEFAULT NULL,
    `target`         int(11)      DEFAULT NULL,
    `type`           int(11)      DEFAULT NULL,
    `path`           varchar(255) DEFAULT NULL,
    `routerName`     varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `index_parent_id` (`parentId`),
    KEY `index_menu_name` (`menuName`),
    KEY `index_enabled` (`enabled`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Inserting 40 rows into sys_menu
-- Insert batch #1
INSERT INTO sys_menu (id, createBy, createTime, lastModifyBy, lastModifyTime, remark, seq, enabled, icon, menuName,
                      parentId, permission, target, type, `path`, routerName)
VALUES (1411967891389157376, 'default', '2021-07-05 16:39:13.058000', 'default', '2021-07-08 10:38:34.493000',
        NULL, 0, 1, 'dashboard', 'Dashboard', 1000000000000000, NULL, 0, 0, '', 'dashboard'),
       (1411968125880111104, 'default', '2021-07-05 16:40:08.966000', NULL, NULL, NULL, 0, 1, '', '工作台',
        1411967891389157376, NULL, 0, 1, '', 'workplace'),
       (1411968251134611456, 'default', '2021-07-05 16:40:38.829000', NULL, NULL, NULL, 0, 1, '', '分析页',
        1411967891389157376, NULL, 0, 1, '', 'analysis'),
       (1412032847849590784, 'default', '2021-07-05 20:57:19.883000', NULL, NULL, NULL, 0, 1, 'form', '表单页',
        1000000000000000, NULL, 0, 0, NULL, 'form'),
       (1412033075436720128, 'default', '2021-07-05 20:58:14.147000', NULL, NULL, NULL, 0, 1, NULL, '基础表单',
        1412032847849590784, NULL, 0, 1, NULL, 'basicForm'),
       (1412033153614352384, 'default', '2021-07-05 20:58:32.786000', NULL, NULL, NULL, 0, 1, NULL, '分步表单',
        1412032847849590784, NULL, 0, 1, NULL, 'stepForm'),
       (1412033236049203200, 'default', '2021-07-05 20:58:52.440000', NULL, NULL, NULL, 0, 1, NULL, '高级表单',
        1412032847849590784, NULL, 0, 1, NULL, 'advanceForm'),
       (1412033612357963776, 'default', '2021-07-05 21:00:22.159000', NULL, NULL, NULL, 0, 1, NULL, '列表页',
        1000000000000000, NULL, 0, 0, NULL, 'list'),
       (1412033752166699008, 'default', '2021-07-05 21:00:55.492000', NULL, NULL, NULL, 0, 1, NULL, '查询表格',
        1412033612357963776, NULL, 0, 1, NULL, 'queryList'),
       (1412033899206414336, 'default', '2021-07-05 21:01:30.549000', NULL, NULL, NULL, 0, 1, NULL, '标准列表',
        1412033612357963776, NULL, 0, 1, NULL, 'primaryList'),
       (1412033972057280512, 'default', '2021-07-05 21:01:47.918000', NULL, NULL, NULL, 0, 1, NULL, '卡片列表',
        1412033612357963776, NULL, 0, 1, NULL, 'cardList'),
       (1412034054844452864, 'default', '2021-07-05 21:02:07.656000', NULL, NULL, NULL, 0, 1, NULL, '搜索列表',
        1412033612357963776, NULL, 0, 1, NULL, 'searchList'),
       (1412034444612734976, 'default', '2021-07-05 21:03:40.584000', NULL, NULL, NULL, 0, 1, NULL, '文章',
        1412034054844452864, NULL, 0, 1, NULL, 'article'),
       (1412034488992665600, 'default', '2021-07-05 21:03:51.165000', NULL, NULL, NULL, 0, 1, NULL, '应用',
        1412034054844452864, NULL, 0, 1, NULL, 'application'),
       (1412034546123280384, 'default', '2021-07-05 21:04:04.786000', NULL, NULL, NULL, 0, 1, NULL, '项目',
        1412034054844452864, NULL, 0, 1, NULL, 'project'),
       (1412034634576957440, 'default', '2021-07-05 21:04:25.875000', NULL, NULL, NULL, 0, 1, NULL, '详情页',
        1000000000000000, NULL, 0, 0, NULL, 'details'),
       (1412034869093076992, 'default', '2021-07-05 21:05:21.788000', NULL, NULL, NULL, 0, 1, NULL, '基础详情页',
        1412034634576957440, NULL, 0, 1, NULL, 'basicDetails'),
       (1412034919290507264, 'default', '2021-07-05 21:05:33.756000', NULL, NULL, NULL, 0, 1, NULL, '高级详情页',
        1412034634576957440, NULL, 0, 1, NULL, 'advanceDetails'),
       (1412035058767892480, 'default', '2021-07-05 21:06:07.010000', NULL, NULL, NULL, 0, 1, NULL, '结果页',
        1000000000000000, NULL, 0, 0, NULL, 'result'),
       (1412035168474107904, 'default', '2021-07-05 21:06:33.165000', NULL, NULL, NULL, 0, 1, NULL, '成功',
        1412035058767892480, NULL, 0, 1, NULL, 'success'),
       (1412035202678657024, 'default', '2021-07-05 21:06:41.321000', NULL, NULL, NULL, 0, 1, NULL, '失败',
        1412035058767892480, NULL, 0, 1, NULL, 'error'),
       (1412035300670181376, 'default', '2021-07-05 21:07:04.684000', 'default', '2021-07-27 11:24:56.470000',
        NULL, 0, 1, NULL, '案例组件', 1000000000000000, NULL, 0, 0, NULL, 'components'),
       (1412035357578498048, 'default', '2021-07-05 21:07:18.252000', NULL, NULL, NULL, 0, 1, NULL, '任务卡片',
        1412035300670181376, NULL, 0, 1, NULL, 'taskCard'),
       (1412035405246763008, 'default', '2021-07-05 21:07:29.617000', NULL, NULL, NULL, 0, 1, NULL, '颜色复选框',
        1412035300670181376, NULL, 0, 1, NULL, 'palette'),
       (1412035699976310784, 'default', '2021-07-05 21:08:39.886000', NULL, NULL, NULL, 0, 1, NULL, '高级表格',
        1412035300670181376, NULL, 0, 1, NULL, 'advanceTable'),
       (1412072708451209216, 'default', '2021-07-05 23:35:43.375000', NULL, NULL, NULL, 0, 1, NULL, '系统管理',
        1000000000000000, NULL, 0, 0, NULL, 'system'),
       (1412073246362308608, 'default', '2021-07-05 23:37:51.640000', NULL, NULL, NULL, 0, 1, NULL, '菜单管理',
        1412072708451209216, NULL, 0, 1, NULL, 'systemMenu'),
       (1413146208779767808, 'default', '2021-07-08 22:41:25.817000', NULL, NULL, NULL, 1, 1, '', '角色管理',
        1412072708451209216, '', 0, 1, '', 'systemRole'),
       (1413146453039255552, 'default', '2021-07-08 22:42:24.053000', NULL, NULL, NULL, 0, 1, '', '用户管理',
        1412072708451209216, '', 0, 1, '', 'systemUser'),
       (1413149986820067328, 'default', '2021-07-08 22:56:26.571000', NULL, NULL, NULL, 0, 1, '', '部门管理',
        1412072708451209216, '', 0, 1, '', 'systemDept'),
       (1413676800764153856, 'default', '2021-07-10 09:49:48.804000', NULL, NULL, NULL, 0, 1, 'monitor', '监控中心',
        1000000000000000, '', 0, 0, '', 'monitor'),
       (1413694425271504896, 'default', '2021-07-10 10:59:50.813000', NULL, NULL, NULL, 0, 1, '', '操作日志',
        1413676800764153856, '', 0, 1, '', 'businessLog'),
       (1416697148505985024, 'default', '2021-07-18 17:51:35.821000', 'default', '2021-07-18 17:55:04.574000',
        NULL, 1, 1, '', 'SwaggerUI', 1413676800764153856, '', 1, 1, 'http://localhost:5935/q/swagger-ui',
        'swaggerUI'),
       (1416938134964408320, 'default', '2021-07-19 09:49:11.470000', NULL, NULL, NULL, 1, 1, '', '字典管理',
        1412072708451209216, '', 0, 1, '', 'systemDict'),
       (1416938453941227520, 'default', '2021-07-19 09:50:27.520000', NULL, NULL, NULL, 0, 1, '', '定时任务',
        1413676800764153856, '', 0, 1, '', 'quartz'),
       (1416938629082779648, 'default', '2021-07-19 09:51:09.277000', 'default', '2021-07-25 16:36:50.396000',
        NULL, 0, 1, '', '参数管理', 1412072708451209216, '', 0, 1, '', 'systemVar'),
       (1417105864329924608, 'default', '2021-07-19 20:55:41.266000', NULL, NULL, NULL, 0, 1, '', '个人中心',
        1412072708451209216, '', 0, 1, '', 'accountCenter'),
       (1418817294058000384, 'default', '2021-07-24 14:16:17.911000', 'default', '2021-07-24 14:19:54.839000',
        NULL, 0, 1, '', 'DevUI', 1413676800764153856, '', 1, 1, 'http://localhost:5935/q/dev/', 'devUI'),
       (1419634538103050240, 'default', '2021-07-26 20:23:44.072000', NULL, NULL, NULL, 0, 1, '', '消息通知',
        1412072708451209216, '', 0, 1, '', 'systemMessage'),
       (1419861613187567616, 'default', '2021-07-27 11:26:02.994000', NULL, NULL, NULL, 0, 1, '', '上传下载',
        1412035300670181376, '', 0, 1, '', 'multipart');

-- END TABLE sys_menu

-- BEGIN TABLE sys_role
DROP TABLE IF EXISTS sys_role;
CREATE TABLE `sys_role`
(
    `id`             bigint(20) NOT NULL,
    `createBy`       varchar(255) DEFAULT NULL,
    `createTime`     datetime(6)  DEFAULT NULL,
    `lastModifyBy`   varchar(255) DEFAULT NULL,
    `lastModifyTime` datetime(6)  DEFAULT NULL,
    `remark`         varchar(255) DEFAULT NULL,
    `seq`            bigint(20)   DEFAULT NULL,
    `enabled`        bit(1)       DEFAULT NULL,
    `roleCode`       varchar(255) DEFAULT NULL,
    `roleName`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `index_role_name` (`roleName`),
    KEY `index_role_code` (`roleCode`),
    KEY `index_enabled` (`enabled`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Inserting 2 rows into sys_role
-- Insert batch #1
INSERT INTO sys_role (id, createBy, createTime, lastModifyBy, lastModifyTime, remark, seq, enabled, roleCode, roleName)
VALUES (1411225867245457408, 'default', '2021-07-03 15:30:40.713000', NULL, NULL, NULL, NULL, 1, 'admin', '超级管理员'),
       (1413149266838425600, 'default', '2021-07-08 22:53:34.914000', 'default', '2021-07-09 15:21:56.455000', NULL, NULL, 1, 'test', '测试员');

-- END TABLE sys_role

-- BEGIN TABLE sys_role_menu
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE `sys_role_menu`
(
    `id`             bigint(20) NOT NULL,
    `createBy`       varchar(255) DEFAULT NULL,
    `createTime`     datetime(6)  DEFAULT NULL,
    `lastModifyBy`   varchar(255) DEFAULT NULL,
    `lastModifyTime` datetime(6)  DEFAULT NULL,
    `remark`         varchar(255) DEFAULT NULL,
    `seq`            bigint(20)   DEFAULT NULL,
    `menuId`         bigint(20)   DEFAULT NULL,
    `roleId`         bigint(20)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `index_role_id` (`roleId`),
    KEY `index_menu_id` (`menuId`),
    CONSTRAINT `FK2lagreeeq77rb54mx3lwlhux4` FOREIGN KEY (`menuId`) REFERENCES `sys_menu` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Inserting 46 rows into sys_role_menu
-- Insert batch #1
INSERT INTO sys_role_menu (id, createBy, createTime, lastModifyBy, lastModifyTime, remark, seq, menuId, roleId)
VALUES (1411968487915655168, 'default', '2021-07-05 16:41:35.281000', NULL, NULL, NULL, NULL, 1411967891389157376,
        1411225867245457408),
       (1411968581582852096, 'default', '2021-07-05 16:41:57.614000', NULL, NULL, NULL, NULL, 1411968125880111104,
        1411225867245457408),
       (1411968610808762368, 'default', '2021-07-05 16:42:04.582000', NULL, NULL, NULL, NULL, 1411968251134611456,
        1411225867245457408),
       (1412039131835535360, 'default', '2021-07-05 21:22:18.086000', NULL, NULL, NULL, 0, 1412032847849590784,
        1411225867245457408),
       (1412039131927810048, 'default', '2021-07-05 21:22:18.127000', NULL, NULL, NULL, 0, 1412033075436720128,
        1411225867245457408),
       (1412039132045250560, 'default', '2021-07-05 21:22:18.155000', NULL, NULL, NULL, 0, 1412033153614352384,
        1411225867245457408),
       (1412039132082999296, 'default', '2021-07-05 21:22:18.164000', NULL, NULL, NULL, 0, 1412033236049203200,
        1411225867245457408),
       (1412039132095582208, 'default', '2021-07-05 21:22:18.167000', NULL, NULL, NULL, 0, 1412033612357963776,
        1411225867245457408),
       (1412039132116553728, 'default', '2021-07-05 21:22:18.172000', NULL, NULL, NULL, 0, 1412033752166699008,
        1411225867245457408),
       (1412039132133330944, 'default', '2021-07-05 21:22:18.176000', NULL, NULL, NULL, 0, 1412033899206414336,
        1411225867245457408),
       (1412039132145913856, 'default', '2021-07-05 21:22:18.179000', NULL, NULL, NULL, 0, 1412033972057280512,
        1411225867245457408),
       (1412039132162691072, 'default', '2021-07-05 21:22:18.183000', NULL, NULL, NULL, 0, 1412034054844452864,
        1411225867245457408),
       (1412039132179468288, 'default', '2021-07-05 21:22:18.187000', NULL, NULL, NULL, 0, 1412034444612734976,
        1411225867245457408),
       (1412039132259160064, 'default', '2021-07-05 21:22:18.206000', NULL, NULL, NULL, 0, 1412034488992665600,
        1411225867245457408),
       (1412039132275937280, 'default', '2021-07-05 21:22:18.210000', NULL, NULL, NULL, 0, 1412034546123280384,
        1411225867245457408),
       (1412039132292714496, 'default', '2021-07-05 21:22:18.214000', NULL, NULL, NULL, 0, 1412034634576957440,
        1411225867245457408),
       (1412039132309491712, 'default', '2021-07-05 21:22:18.217000', NULL, NULL, NULL, 0, 1412034869093076992,
        1411225867245457408),
       (1412039132322074624, 'default', '2021-07-05 21:22:18.221000', NULL, NULL, NULL, 0, 1412034919290507264,
        1411225867245457408),
       (1412039132334657536, 'default', '2021-07-05 21:22:18.224000', NULL, NULL, NULL, 0, 1412035058767892480,
        1411225867245457408),
       (1412039132351434752, 'default', '2021-07-05 21:22:18.228000', NULL, NULL, NULL, 0, 1412035168474107904,
        1411225867245457408),
       (1412039132364017664, 'default', '2021-07-05 21:22:18.231000', NULL, NULL, NULL, 0, 1412035202678657024,
        1411225867245457408),
       (1412039132376600576, 'default', '2021-07-05 21:22:18.234000', NULL, NULL, NULL, 0, 1412035300670181376,
        1411225867245457408),
       (1412039132464680960, 'default', '2021-07-05 21:22:18.255000', NULL, NULL, NULL, 0, 1412035357578498048,
        1411225867245457408),
       (1412039132477263872, 'default', '2021-07-05 21:22:18.258000', NULL, NULL, NULL, 0, 1412035405246763008,
        1411225867245457408),
       (1412039132485652480, 'default', '2021-07-05 21:22:18.260000', NULL, NULL, NULL, 0, 1412035699976310784,
        1411225867245457408),
       (1412073320261750784, 'default', '2021-07-05 23:38:09.240000', NULL, NULL, NULL, 0, 1412072708451209216,
        1411225867245457408),
       (1412073320349831168, 'default', '2021-07-05 23:38:09.282000', NULL, NULL, NULL, 0, 1412073246362308608,
        1411225867245457408),
       (1413147559328878592, 'default', '2021-07-08 22:46:47.813000', NULL, NULL, NULL, NULL, 1413146208779767808,
        1411225867245457408),
       (1413147592065421312, 'default', '2021-07-08 22:46:55.618000', NULL, NULL, NULL, NULL, 1413146453039255552,
        1411225867245457408),
       (1413392566988181506, 'default', '2021-07-09 15:00:22.191000', NULL, NULL, NULL, 0, 1412033153614352384,
        1413149266838425600),
       (1413392566988181507, 'default', '2021-07-09 15:00:22.191000', NULL, NULL, NULL, 0, 1412033236049203200,
        1413149266838425600),
       (1413394799175798784, 'default', '2021-07-09 15:09:14.386000', NULL, NULL, NULL, 0, 1411967891389157376,
        1413149266838425600),
       (1413394799175798785, 'default', '2021-07-09 15:09:14.386000', NULL, NULL, NULL, 0, 1411968125880111104,
        1413149266838425600),
       (1413394799175798786, 'default', '2021-07-09 15:09:14.386000', NULL, NULL, NULL, 0, 1411968251134611456,
        1413149266838425600),
       (1413396681914978304, 'default', '2021-07-09 15:16:43.266000', NULL, NULL, NULL, 0, 1412032847849590784,
        1413149266838425600),
       (1413694607363018752, 'default', '2021-07-10 11:00:34.228000', NULL, NULL, NULL, 0, 1413694425271504896,
        1411225867245457408),
       (1413694607363018753, 'default', '2021-07-10 11:00:34.228000', NULL, NULL, NULL, 0, 1413676800764153856,
        1411225867245457408),
       (1413751410792730624, 'default', '2021-07-10 14:46:17.220000', NULL, NULL, NULL, 0, 1413149986820067328,
        1411225867245457408),
       (1416697723008192512, 'default', '2021-07-18 17:53:52.793000', NULL, NULL, NULL, 0, 1416697148505985024,
        1411225867245457408),
       (1416939339576905728, 'default', '2021-07-19 09:53:58.672000', NULL, NULL, NULL, 0, 1416938134964408320,
        1411225867245457408),
       (1416939339576905729, 'default', '2021-07-19 09:53:58.672000', NULL, NULL, NULL, 0, 1416938629082779648,
        1411225867245457408),
       (1416939339576905730, 'default', '2021-07-19 09:53:58.672000', NULL, NULL, NULL, 0, 1416938453941227520,
        1411225867245457408),
       (1417105892482093056, 'default', '2021-07-19 20:55:47.979000', NULL, NULL, NULL, 0, 1417105864329924608,
        1411225867245457408),
       (1418818063847002112, 'default', '2021-07-24 14:19:21.443000', NULL, NULL, NULL, 0, 1418817294058000384,
        1411225867245457408),
       (1419634863056752640, 'default', '2021-07-26 20:25:01.550000', NULL, NULL, NULL, 0, 1419634538103050240,
        1411225867245457408),
       (1419861657240342528, 'default', '2021-07-27 11:26:13.497000', NULL, NULL, NULL, 0, 1419861613187567616,
        1411225867245457408);

-- END TABLE sys_role_menu

-- BEGIN TABLE sys_user
DROP TABLE IF EXISTS sys_user;
CREATE TABLE `sys_user`
(
    `id`             bigint(20) NOT NULL,
    `createBy`       varchar(255) DEFAULT NULL,
    `createTime`     datetime(6)  DEFAULT NULL,
    `lastModifyBy`   varchar(255) DEFAULT NULL,
    `lastModifyTime` datetime(6)  DEFAULT NULL,
    `remark`         varchar(255) DEFAULT NULL,
    `avatar`         varchar(255) DEFAULT NULL,
    `enabled`        bit(1)       DEFAULT NULL,
    `lastLoginIp`    varchar(255) DEFAULT NULL,
    `lastLoginTime`  varchar(255) DEFAULT NULL,
    `password`       varchar(255) DEFAULT NULL,
    `phone`          varchar(255) DEFAULT NULL,
    `username`       varchar(255) DEFAULT NULL,
    `seq`            bigint(20)   DEFAULT NULL,
    `email`          varchar(255) DEFAULT NULL,
    `description`    varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `index_username` (`username`),
    KEY `index_phone` (`phone`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Inserting 3 rows into sys_user
-- Insert batch #1
INSERT INTO sys_user (id, createBy, createTime, lastModifyBy, lastModifyTime, remark, avatar, enabled, lastLoginIp,
                      lastLoginTime, password, phone, username, seq, email, description)
VALUES (1410480749857083392, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, '10000123123', '管理员', NULL,
        NULL, NULL),
       (1410482993549676544, 'someone', '2021-07-01 14:18:45.811000', 'default', '2021-07-11 20:24:34.312000',
        NULL, 'https://aaa.jpg', 1, NULL, NULL, NULL, '18018166272', '新的名字', NULL, NULL, NULL),
       (1411221497711824896, 'default', '2021-07-03 15:13:18.939000', 'default', '2021-07-24 14:08:52.550000',
        NULL, 'avatar-20216-pfx15237578068552959433sfx', 1, NULL, NULL, '670b14728ad9902aecba32e22fa4f6bd',
        '18018266280', '嘎嘣脆3', NULL, '123', '');

-- END TABLE sys_user

-- BEGIN TABLE sys_user_dept
DROP TABLE IF EXISTS sys_user_dept;
CREATE TABLE `sys_user_dept`
(
    `id`             bigint(20) NOT NULL,
    `createBy`       varchar(255) DEFAULT NULL,
    `createTime`     datetime(6)  DEFAULT NULL,
    `lastModifyBy`   varchar(255) DEFAULT NULL,
    `lastModifyTime` datetime(6)  DEFAULT NULL,
    `remark`         varchar(255) DEFAULT NULL,
    `seq`            bigint(20)   DEFAULT NULL,
    `deptId`         bigint(20)   DEFAULT NULL,
    `userId`         bigint(20)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `index_user_id` (`userId`),
    KEY `index_dept_id` (`deptId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Inserting 2 rows into sys_user_dept
-- Insert batch #1
INSERT INTO sys_user_dept (id, createBy, createTime, lastModifyBy, lastModifyTime, remark, seq, deptId, userId)
VALUES (1414198872833658880, 'default', '2021-07-11 20:24:20.485000', NULL, NULL, NULL, 0, 1413756452069314560,
        1410480749857083392),
       (1414198962805673984, 'default', '2021-07-11 20:24:41.936000', NULL, NULL, NULL, 0, 1413756452069314560,
        1411221497711824896);

-- END TABLE sys_user_dept

-- BEGIN TABLE sys_user_role
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE `sys_user_role`
(
    `id`             bigint(20) NOT NULL,
    `createBy`       varchar(255) DEFAULT NULL,
    `createTime`     datetime(6)  DEFAULT NULL,
    `lastModifyBy`   varchar(255) DEFAULT NULL,
    `lastModifyTime` datetime(6)  DEFAULT NULL,
    `remark`         varchar(255) DEFAULT NULL,
    `seq`            bigint(20)   DEFAULT NULL,
    `roleId`         bigint(20)   DEFAULT NULL,
    `userId`         bigint(20)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `index_role_id` (`roleId`),
    KEY `index_user_id` (`userId`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Inserting 8 rows into sys_user_role
-- Insert batch #1
INSERT INTO sys_user_role (id, createBy, createTime, lastModifyBy, lastModifyTime, remark, seq, roleId, userId)
VALUES (1411226649567039488, 'default', '2021-07-03 15:33:47.237000', NULL, NULL, NULL, NULL, 1411225867245457408,
        1411221497711824896),
       (1414198872976265216, 'default', '2021-07-11 20:24:20.519000', NULL, NULL, NULL, 0, 1413149266838425600,
        1410480749857083392),
       (1414198930916380672, 'default', '2021-07-11 20:24:34.333000', NULL, NULL, NULL, 0, 1411225867245457408,
        1410482993549676544),
       (1414202350683426816, 'default', '2021-07-11 20:38:09.669000', NULL, NULL, NULL, 0, 1411225867245457408,
        NULL),
       (1414204694879932416, 'default', '2021-07-11 20:47:28.569000', NULL, NULL, NULL, 0, 1413149266838425600,
        NULL),
       (1414204755294687232, 'default', '2021-07-11 20:47:42.973000', NULL, NULL, NULL, 0, 1411225867245457408,
        NULL),
       (1414204755298881536, 'default', '2021-07-11 20:47:42.974000', NULL, NULL, NULL, 0, 1413149266838425600,
        NULL),
       (1414222284935270400, 'default', '2021-07-11 21:57:22.365000', NULL, NULL, NULL, 0, 1413149266838425600,
        1414222284889133056);

-- END TABLE sys_user_role

-- BEGIN TABLE sys_var
DROP TABLE IF EXISTS sys_var;
CREATE TABLE `sys_var`
(
    `id`             bigint(20) NOT NULL,
    `createBy`       varchar(255) DEFAULT NULL,
    `createTime`     datetime(6)  DEFAULT NULL,
    `lastModifyBy`   varchar(255) DEFAULT NULL,
    `lastModifyTime` datetime(6)  DEFAULT NULL,
    `remark`         varchar(255) DEFAULT NULL,
    `seq`            bigint(20)   DEFAULT NULL,
    `varCode`        varchar(255) DEFAULT NULL,
    `varName`        varchar(255) DEFAULT NULL,
    `varValue`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `index_var_code` (`varCode`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- Inserting 1 row into sys_var
-- Insert batch #1
INSERT INTO sys_var (id, createBy, createTime, lastModifyBy, lastModifyTime, remark, seq, varCode, varName, varValue)
VALUES (1419216779036725248, 'default', '2021-07-25 16:43:42.551000', NULL, NULL, NULL, NULL, 'test', '测试', 'something');

-- END TABLE sys_var

