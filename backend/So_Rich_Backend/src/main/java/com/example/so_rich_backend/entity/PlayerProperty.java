package com.example.so_rich_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 玩家房产表实体
 */
@Data
@TableName("player_property")
public class PlayerProperty {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long roomId;

    private Long playerId;

    private Integer cellIndex;

    private Integer houseLevel;

    private Integer isMortgaged;

    private LocalDateTime acquiredAt;
}
