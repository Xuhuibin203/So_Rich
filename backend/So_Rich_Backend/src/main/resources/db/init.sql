-- =============================================
-- So Rich 大富翁 数据库初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS so_rich
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE so_rich;

-- ----------------------------
-- 1. 用户表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user` (
    `id`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户唯一标识',
    `username`   VARCHAR(50)  NOT NULL                COMMENT '登录用户名',
    `password`   VARCHAR(255) NOT NULL                COMMENT '加密后的密码(BCrypt)',
    `nickname`   VARCHAR(50)  NOT NULL                COMMENT '游戏内显示昵称',
    `avatar`     VARCHAR(255) DEFAULT NULL            COMMENT '头像URL',
    `created_at` DATETIME     NOT NULL DEFAULT NOW()  COMMENT '注册时间',
    `updated_at` DATETIME     NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '最后修改时间',
    `deleted`    TINYINT      NOT NULL DEFAULT 0      COMMENT '逻辑删除(0=正常,1=删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ----------------------------
-- 2. 房间表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `room` (
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '房间唯一标识',
    `room_code`   VARCHAR(6)  NOT NULL                COMMENT '6位房间号',
    `name`        VARCHAR(50) NOT NULL                COMMENT '房间名称',
    `host_id`     BIGINT      NOT NULL                COMMENT '房主用户ID',
    `max_players` INT         NOT NULL DEFAULT 4      COMMENT '最大玩家数(2-6)',
    `status`      VARCHAR(20) NOT NULL DEFAULT 'WAITING' COMMENT '状态: WAITING/PLAYING/FINISHED',
    `created_at`  DATETIME    NOT NULL DEFAULT NOW()  COMMENT '创建时间',
    `updated_at`  DATETIME    NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '最后修改时间',
    `deleted`     TINYINT     NOT NULL DEFAULT 0      COMMENT '逻辑删除(0=正常,1=删除)',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_room_code` (`room_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房间表';

-- ----------------------------
-- 3. 房间玩家表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `room_player` (
    `id`           BIGINT        NOT NULL AUTO_INCREMENT COMMENT '记录唯一标识',
    `room_id`      BIGINT        NOT NULL                COMMENT '所属房间ID',
    `user_id`      BIGINT        NOT NULL                COMMENT '玩家用户ID',
    `player_order` INT           NOT NULL                COMMENT '玩家顺序(1-6)',
    `money`        DECIMAL(15,2) NOT NULL DEFAULT 1500.00 COMMENT '当前持有现金',
    `position`     INT           NOT NULL DEFAULT 0      COMMENT '当前位置(格子索引0-39)',
    `status`       VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE/BANKRUPT/LEFT',
    `color`        VARCHAR(10)   DEFAULT NULL            COMMENT '棋子颜色',
    `joined_at`    DATETIME      NOT NULL DEFAULT NOW()  COMMENT '加入时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_room_user` (`room_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房间玩家表';

-- ----------------------------
-- 4. 棋盘格子表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `board_cell` (
    `id`             BIGINT        NOT NULL AUTO_INCREMENT COMMENT '格子唯一标识',
    `board_version`  VARCHAR(20)   NOT NULL DEFAULT 'v1'  COMMENT '棋盘版本号',
    `cell_index`     INT           NOT NULL                COMMENT '格子位置(0-39)',
    `name`           VARCHAR(50)   NOT NULL                COMMENT '格子名称',
    `type`           VARCHAR(20)   NOT NULL                COMMENT '格子类型: PROPERTY/CHANCE/FATE/TAX/START/JAIL/FREE_PARKING/GO_TO_JAIL/RAILROAD/UTILITY',
    `group_id`       INT           DEFAULT NULL            COMMENT '地产颜色分组(1-8)',
    `price`          DECIMAL(10,2) DEFAULT NULL            COMMENT '购买价格',
    `house_cost`     DECIMAL(10,2) DEFAULT NULL            COMMENT '单栋房屋建造费用',
    `rent_0`         DECIMAL(10,2) DEFAULT NULL            COMMENT '空地租金',
    `rent_1`         DECIMAL(10,2) DEFAULT NULL            COMMENT '1栋房屋租金',
    `rent_2`         DECIMAL(10,2) DEFAULT NULL            COMMENT '2栋房屋租金',
    `rent_3`         DECIMAL(10,2) DEFAULT NULL            COMMENT '3栋房屋租金',
    `rent_hotel`     DECIMAL(10,2) DEFAULT NULL            COMMENT '旅馆(4栋)租金',
    `mortgage_value` DECIMAL(10,2) DEFAULT NULL            COMMENT '抵押价值',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_board_cell` (`board_version`, `cell_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='棋盘格子表';

-- ----------------------------
-- 5. 玩家房产表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `player_property` (
    `id`           BIGINT     NOT NULL AUTO_INCREMENT COMMENT '记录唯一标识',
    `room_id`      BIGINT     NOT NULL               COMMENT '所属房间ID',
    `player_id`    BIGINT     NOT NULL               COMMENT '所属玩家记录ID(room_player.id)',
    `cell_index`   INT        NOT NULL               COMMENT '对应棋盘格子编号(0-39)',
    `house_level`  INT        NOT NULL DEFAULT 0     COMMENT '房屋等级(0=空地,1-4=房屋,4=旅馆)',
    `is_mortgaged` TINYINT    NOT NULL DEFAULT 0     COMMENT '是否抵押(0=未抵押,1=已抵押)',
    `acquired_at`  DATETIME   NOT NULL DEFAULT NOW() COMMENT '获得时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_room_cell` (`room_id`, `cell_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='玩家房产表';
