# board_cell 棋盘格子表

## 目的
存储棋盘上每个格子的静态属性（名称、类型、价格、租金等），作为棋盘配置数据，不随对局变化。

## 字段设计

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 格子唯一标识 |
| board_version | VARCHAR(20) | NOT NULL, DEFAULT 'v1' | 棋盘版本号 |
| cell_index | INT | NOT NULL | 格子位置(0-39) |
| name | VARCHAR(50) | NOT NULL | 格子名称(如"地中海大道") |
| type | VARCHAR(20) | NOT NULL | 格子类型: PROPERTY/CHANCE/FATE/TAX/START/JAIL/FREE_PARKING/GO_TO_JAIL/RAILROAD/UTILITY |
| group_id | INT | NULL | 地产颜色分组(1-8, 同色系3块地) |
| price | DECIMAL(10,2) | NULL | 购买价格(非地产类为NULL) |
| house_cost | DECIMAL(10,2) | NULL | 单栋房屋建造费用 |
| rent_0 | DECIMAL(10,2) | NULL | 空地租金 |
| rent_1 | DECIMAL(10,2) | NULL | 1栋房屋租金 |
| rent_2 | DECIMAL(10,2) | NULL | 2栋房屋租金 |
| rent_3 | DECIMAL(10,2) | NULL | 3栋房屋租金 |
| rent_hotel | DECIMAL(10,2) | NULL | 旅馆(4栋)租金 |
| mortgage_value | DECIMAL(10,2) | NULL | 抵押价值 |

## 备注
- 标准大富翁棋盘40格（cell_index 0-39）
- 格0: 起点 START
- 格10: 监狱探视 JAIL
- 格20: 免费停车 FREE_PARKING
- 格30: 前往监狱 GO_TO_JAIL
- 地产类型(PROPERTY)需要完整的租金梯度
- 机会卡(CHANCE)和命运卡(FATE)触发随机事件，不在此表存储
