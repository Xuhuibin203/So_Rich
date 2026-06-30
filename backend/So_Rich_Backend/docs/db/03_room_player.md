# room_player 房间玩家表

## 目的
记录每个房间内的玩家信息，包括玩家在当前对局中的金钱和状态。每局独立，玩家退出后该房间记录保留用于复盘。

## 字段设计

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 记录唯一标识 |
| room_id | BIGINT | NOT NULL, FK→room.id | 所属房间ID |
| user_id | BIGINT | NOT NULL, FK→user.id | 玩家用户ID |
| player_order | INT | NOT NULL | 玩家顺序(1-6, 决定回合顺序) |
| money | DECIMAL(15,2) | NOT NULL, DEFAULT 1500.00 | 当前持有现金 |
| position | INT | NOT NULL, DEFAULT 0 | 当前位置(格子索引0-39) |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'ACTIVE' | 状态: ACTIVE/BANKRUPT/LEFT |
| color | VARCHAR(10) | NULL | 棋子颜色(如 red/blue/green/yellow) |
| joined_at | DATETIME | NOT NULL, DEFAULT NOW() | 加入时间 |

## 备注
- 每个用户在同一房间只有一条记录 (room_id + user_id 联合唯一)
- money <= 0 且无法支付时，status 变更为 BANKRUPT（破产）
- 所有玩家破产或仅剩一人时游戏结束
- 初始现金默认 1500（经典规则）
- 棋盘40格（0-39），起点为格子0
