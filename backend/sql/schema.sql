CREATE DATABASE IF NOT EXISTS `kaoyan_system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `kaoyan_system`;

CREATE TABLE `user` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username`    VARCHAR(20)  NOT NULL COMMENT '用户名',
  `password`    VARCHAR(100) NOT NULL COMMENT 'BCrypt密文',
  `email`       VARCHAR(50)  NOT NULL COMMENT '邮箱(登录凭证)',
  `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号(预留)',
  `nickname`    VARCHAR(30)  DEFAULT NULL COMMENT '昵称',
  `avatar_url`  VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
  `intro`       VARCHAR(200) DEFAULT NULL COMMENT '个人简介',
  `exam_type`   TINYINT      DEFAULT 0 COMMENT '备考方向 0无/1考研/2考公',
  `identity`     TINYINT      DEFAULT 1 COMMENT '身份 1考生/2督学',
  `role`        TINYINT      NOT NULL DEFAULT 1 COMMENT '角色 0管理员/1普通用户',
  `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态 0禁用/1正常',
  `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `goal` (
  `id`          BIGINT      NOT NULL AUTO_INCREMENT,
  `user_id`     BIGINT      NOT NULL COMMENT '所属用户',
  `exam_type`   TINYINT     NOT NULL COMMENT '1考研/2考公',
  `target_name` VARCHAR(100) DEFAULT NULL COMMENT '目标院校/岗位',
  `major`       VARCHAR(100) DEFAULT NULL COMMENT '专业(考研)',
  `position`    VARCHAR(100) DEFAULT NULL COMMENT '岗位类型(考公)',
  `subjects`    VARCHAR(200) DEFAULT NULL COMMENT '考试科目JSON数组',
  `target_score` VARCHAR(50) DEFAULT NULL COMMENT '目标分数/分数线',
  `exam_date`   DATE        NOT NULL COMMENT '考试日期',
  `status`      TINYINT     DEFAULT 1 COMMENT '1进行中/2已结束',
  `deleted`     TINYINT     NOT NULL DEFAULT 0,
  `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='备考目标表';

CREATE TABLE `plan_phase` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT,
  `goal_id`     BIGINT       NOT NULL COMMENT '所属目标',
  `phase_name`  VARCHAR(50)  NOT NULL COMMENT '阶段名',
  `start_date`  DATE         NOT NULL,
  `end_date`    DATE         NOT NULL,
  `phase_target` VARCHAR(200) DEFAULT NULL COMMENT '阶段目标',
  `deleted`     TINYINT      NOT NULL DEFAULT 0,
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_goal_id` (`goal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='阶段计划表';

CREATE TABLE `plan_task` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT,
  `user_id`      BIGINT       NOT NULL,
  `goal_id`      BIGINT       NOT NULL,
  `phase_id`     BIGINT       DEFAULT NULL COMMENT '所属阶段可空',
  `subject`      VARCHAR(50)  NOT NULL COMMENT '科目',
  `task_content` VARCHAR(500) NOT NULL COMMENT '任务内容',
  `plan_date`    DATE         NOT NULL COMMENT '计划日期',
  `plan_duration` INT         DEFAULT NULL COMMENT '预计时长(分钟)',
  `priority`     TINYINT      DEFAULT 2 COMMENT '1高/2中/3低',
  `status`       TINYINT      DEFAULT 0 COMMENT '0未开始/1已完成/2已过期',
  `deleted`      TINYINT      NOT NULL DEFAULT 0,
  `create_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_date` (`user_id`, `plan_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习任务表';

CREATE TABLE `checkin_record` (
  `id`             BIGINT   NOT NULL AUTO_INCREMENT,
  `user_id`        BIGINT   NOT NULL,
  `task_id`        BIGINT   DEFAULT NULL COMMENT '关联任务',
  `checkin_date`   DATE     NOT NULL COMMENT '打卡日期',
  `actual_duration` INT     DEFAULT NULL COMMENT '实际时长(分钟)',
  `note`           VARCHAR(200) DEFAULT NULL,
  `is_makeup`      TINYINT  DEFAULT 0 COMMENT '是否补卡',
  `create_time`    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_task_date` (`user_id`, `task_id`, `checkin_date`),
  KEY `idx_user_date` (`user_id`, `checkin_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡记录表';

CREATE TABLE `study_record` (
  `id`          BIGINT   NOT NULL AUTO_INCREMENT,
  `user_id`     BIGINT   NOT NULL,
  `subject`     VARCHAR(50) NOT NULL,
  `duration`    INT      NOT NULL COMMENT '时长(分钟)',
  `record_date` DATE     NOT NULL,
  `source`      TINYINT  NOT NULL COMMENT '1任务打卡/2番茄钟/3手动',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_date` (`user_id`, `record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习记录表';

CREATE TABLE `pomodoro` (
  `id`         BIGINT   NOT NULL AUTO_INCREMENT,
  `user_id`    BIGINT   NOT NULL,
  `subject`    VARCHAR(50) DEFAULT NULL,
  `duration`   INT      NOT NULL COMMENT '专注时长(分钟)',
  `start_time` DATETIME NOT NULL,
  `end_time`   DATETIME NOT NULL,
  `status`     TINYINT  DEFAULT 1 COMMENT '1完成/2中断',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='番茄钟记录表';

CREATE TABLE `material` (
  `id`             BIGINT       NOT NULL AUTO_INCREMENT,
  `title`          VARCHAR(100) NOT NULL,
  `category`       VARCHAR(50)  NOT NULL COMMENT '分类',
  `file_url`       VARCHAR(255) NOT NULL COMMENT '文件地址',
  `uploader_id`    BIGINT       NOT NULL,
  `download_count` INT          DEFAULT 0,
  `status`         TINYINT      DEFAULT 0 COMMENT '0待审核/1已发布/2已下架',
  `deleted`        TINYINT      NOT NULL DEFAULT 0,
  `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资料表';

CREATE TABLE `post` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT,
  `user_id`       BIGINT       NOT NULL,
  `title`         VARCHAR(100) NOT NULL,
  `content`       TEXT         NOT NULL,
  `like_count`    INT          DEFAULT 0,
  `comment_count` INT          DEFAULT 0,
  `status`        TINYINT      DEFAULT 0 COMMENT '0待审核/1已发布/2已下架',
  `deleted`       TINYINT      NOT NULL DEFAULT 0,
  `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_status_time` (`status`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

CREATE TABLE `comment` (
  `id`          BIGINT   NOT NULL AUTO_INCREMENT,
  `post_id`     BIGINT   NOT NULL,
  `user_id`     BIGINT   NOT NULL,
  `content`     VARCHAR(500) NOT NULL,
  `deleted`     TINYINT  NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

CREATE TABLE `follow` (
  `id`          BIGINT   NOT NULL AUTO_INCREMENT,
  `follower_id` BIGINT   NOT NULL COMMENT '关注者(督学)',
  `followed_id` BIGINT   NOT NULL COMMENT '被关注者(考生)',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_follow` (`follower_id`, `followed_id`),
  KEY `idx_followed` (`followed_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关注关系表';

CREATE TABLE `help_post` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT,
  `user_id`       BIGINT       NOT NULL COMMENT '求助者(考生)',
  `title`         VARCHAR(100) NOT NULL,
  `content`       TEXT         NOT NULL,
  `subject`       VARCHAR(50)  DEFAULT NULL,
  `target_user_id` BIGINT      DEFAULT NULL COMMENT '@指定督学',
  `status`        TINYINT      DEFAULT 1 COMMENT '1待回复/2已回复/3已关闭',
  `deleted`       TINYINT      NOT NULL DEFAULT 0,
  `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target` (`target_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='求助帖表';

CREATE TABLE `help_reply` (
  `id`          BIGINT   NOT NULL AUTO_INCREMENT,
  `help_id`     BIGINT   NOT NULL,
  `user_id`     BIGINT   NOT NULL,
  `content`     VARCHAR(500) NOT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_help_id` (`help_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='求助回复表';

CREATE TABLE `notification` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT,
  `user_id`       BIGINT       NOT NULL COMMENT '接收者',
  `from_user_id`  BIGINT       DEFAULT NULL COMMENT '触发者',
  `type`          TINYINT      NOT NULL COMMENT '1打卡动态/2学习求助/3求助回复/4监督提醒/5审核结果/6任务提醒/7倒计时/8断卡/9系统公告',
  `content`       VARCHAR(500) NOT NULL,
  `ref_id`        BIGINT       DEFAULT NULL COMMENT '关联业务对象ID',
  `is_read`       TINYINT      DEFAULT 0 COMMENT '0未读/1已读',
  `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_read` (`user_id`, `is_read`),
  KEY `idx_user_time` (`user_id`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

CREATE TABLE `audit_log` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT,
  `operator_id` BIGINT       NOT NULL COMMENT '操作人',
  `action`      VARCHAR(50)  NOT NULL COMMENT '操作',
  `target_type` VARCHAR(30)  DEFAULT NULL,
  `target_id`   BIGINT       DEFAULT NULL,
  `detail`      VARCHAR(500) DEFAULT NULL,
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_operator` (`operator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
