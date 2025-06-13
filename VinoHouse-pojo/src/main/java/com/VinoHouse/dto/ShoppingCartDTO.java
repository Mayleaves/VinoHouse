package com.VinoHouse.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    private Long dishId;  // 前端硬编码，不能修改为 beverageId
    private Long setmealId;
    private String dishFlavor;  // 前端硬编码，不能修改为 beverageFlavor

}
