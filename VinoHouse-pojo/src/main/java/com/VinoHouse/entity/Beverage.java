package com.VinoHouse.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 酒水
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Beverage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 酒水名称
    private String name;

    // 酒水分类 id
    private Long categoryId;

    // 酒水价格
    private BigDecimal price;

    // 图片
    private String image;

    // 描述信息
    private String description;

    // 0停售 1起售
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
