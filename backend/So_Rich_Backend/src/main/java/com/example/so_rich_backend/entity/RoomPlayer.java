package com.example.so_rich_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 房间玩家表实体
 */
@Data
@TableName("room_player")
public class RoomPlayer {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long roomId;

    private Long userId;

    private Integer playerOrder;

    private BigDecimal money;

    private Integer position;

    private String status;

    private String color;

    private LocalDateTime joinedAt;
}
