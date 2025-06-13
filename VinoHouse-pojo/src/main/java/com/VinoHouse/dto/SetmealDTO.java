package com.VinoHouse.dto;

import com.VinoHouse.entity.SetmealBeverage;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class SetmealDTO implements Serializable {

    private Long id;

    // 分类 id
    private Long categoryId;

    // 套餐名称
    private String name;

    // 套餐价格
    private BigDecimal price;

    // 状态 0:停用 1:启用
    private Integer status;

    // 描述信息
    private String description;

    // 图片
    private String image;

    // 套餐酒水关系
    private List<SetmealBeverage> setmealDishes = new ArrayList<>();  // 这里的 setmealDishes 不能修改，要和前端保持一致 json

}
