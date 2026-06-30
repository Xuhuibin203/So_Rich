# room 房间表

## 目的
存储游戏房间信息，管理房间状态和创建者。

## 字段设计

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 房间唯一标识 |
| room_code | VARCHAR(6) | NOT NULL, UNIQUE | 6位房间号(字母数字) |
| name | VARCHAR(50) | NOT NULL | 房间名称 |
| host_id | BIGINT | NOT NULL, FK→user.id | 房主用户ID |
| max_players | INT | NOT NULL, DEFAULT 4 | 最大玩家数(2-6) |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'WAITING' | 状态: WAITING/PLAYING/FINISHED |
| board_id | BIGINT | NULL, FK→board_cell的配置版本 | 使用的棋盘配置ID |
| created_at | DATETIME | NOT NULL, DEFAULT NOW() | 创建时间 |
| updated_at | DATETIME | NOT NULL, DEFAULT NOW() | 最后修改时间 |
| deleted | TINYINT | NOT NULL, DEFAULT 0 | 逻辑删除标记 |

## 备注
- WAITING: 等待玩家加入
- PLAYING: 游戏进行中
- FINISHED: 游戏结束
- 房间号6位随机生成，用于玩家加入
