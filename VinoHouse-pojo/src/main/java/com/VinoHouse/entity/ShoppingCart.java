package com.VinoHouse.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 名称
    private String name;

    // 用户id
    private Long userId;

    // 酒水id
    private Long beverageId;

    // 套餐id
    private Long setmealId;

    // 口味
    private String beverageFlavor;

    // 数量
    private Integer number;

    // 金额
    private BigDecimal amount;

    // 图片
    private String image;

    private LocalDateTime createTime;
}
