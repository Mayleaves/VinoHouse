package com.VinoHouse.dto;

import com.VinoHouse.entity.BeverageFlavor;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class BeverageDTO implements Serializable {

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
    // 0 停售 1 起售
    private Integer status;
    // 口味
    private List<BeverageFlavor> flavors = new ArrayList<>();

}
