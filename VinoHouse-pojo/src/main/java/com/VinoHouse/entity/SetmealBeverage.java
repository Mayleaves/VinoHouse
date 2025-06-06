package com.VinoHouse.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 套餐酒水关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealBeverage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 套餐id
    private Long setmealId;

    // 酒水id
    private Long beverageId;

    // 酒水名称 （冗余字段）
    private String name;

    // 酒水原价
    private BigDecimal price;

    // 份数
    private Integer copies;
}
