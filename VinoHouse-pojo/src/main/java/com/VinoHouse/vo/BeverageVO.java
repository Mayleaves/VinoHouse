package com.VinoHouse.vo;

import com.VinoHouse.entity.BeverageFlavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeverageVO implements Serializable {

    private Long id;

    // 酒水名称
    private String name;

    // 酒水分类id
    private Long categoryId;

    // 酒水价格
    private BigDecimal price;

    // 图片
    private String image;

    // 描述信息
    private String description;

    // 0停售 1起售
    private Integer status;

    // 更新时间
    private LocalDateTime updateTime;

    // 分类名称：BeverageDTO 没有的部分
    private String categoryName;

    // 酒水关联的口味
    private List<BeverageFlavor> flavors = new ArrayList<>();

}
