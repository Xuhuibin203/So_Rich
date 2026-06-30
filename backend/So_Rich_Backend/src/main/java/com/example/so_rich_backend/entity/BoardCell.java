package com.example.so_rich_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 棋盘格子表实体
 */
@Data
@TableName("board_cell")
public class BoardCell {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String boardVersion;

    private Integer cellIndex;

    private String name;

    private String type;

    private Integer groupId;

    private BigDecimal price;

    private BigDecimal houseCost;

    private BigDecimal rent0;

    private BigDecimal rent1;

    private BigDecimal rent2;

    private BigDecimal rent3;

    private BigDecimal rentHotel;

    private BigDecimal mortgageValue;
}
