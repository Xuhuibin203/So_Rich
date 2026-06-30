# player_property 玩家房产表

## 目的
记录玩家在每局游戏中拥有的地产及房屋等级，是游戏核心资产数据。

## 字段设计

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 记录唯一标识 |
| room_id | BIGINT | NOT NULL, FK→room.id | 所属房间ID |
| player_id | BIGINT | NOT NULL, FK→room_player.id | 所属玩家记录ID |
| cell_index | INT | NOT NULL | 对应棋盘格子编号(0-39) |
| house_level | INT | NOT NULL, DEFAULT 0 | 房屋等级: 0=空地, 1-4=房屋数, 4=旅馆 |
| is_mortgaged | TINYINT | NOT NULL, DEFAULT 0 | 是否抵押: 0=未抵押, 1=已抵押 |
| acquired_at | DATETIME | NOT NULL, DEFAULT NOW() | 获得时间 |

## 备注
- (room_id, cell_index) 联合唯一，同一房间一块地只能属于一个玩家
- house_level 限制: 0-4 (含旅馆)
- 建造房屋需满足：拥有同色组全部地产 + 均匀建造
- 抵押后不能收租，需付利息赎回
- 该表与 room_player 共同决定玩家资产总值
