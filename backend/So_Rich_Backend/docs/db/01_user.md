# user 用户表

## 目的
存储系统用户的基本信息，支持登录认证和游戏昵称展示。

## 字段设计

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 用户唯一标识 |
| username | VARCHAR(50) | NOT NULL, UNIQUE | 登录用户名 |
| password | VARCHAR(255) | NOT NULL | 加密后的密码 |
| nickname | VARCHAR(50) | NOT NULL | 游戏内显示昵称 |
| avatar | VARCHAR(255) | NULL | 头像URL |
| created_at | DATETIME | NOT NULL, DEFAULT NOW() | 注册时间 |
| updated_at | DATETIME | NOT NULL, DEFAULT NOW() | 最后修改时间 |
| deleted | TINYINT | NOT NULL, DEFAULT 0 | 逻辑删除标记(0=正常,1=删除) |

## 备注
- 密码使用 BCrypt 加密存储
- 逻辑删除配合 MyBatis-Plus 自动过滤
