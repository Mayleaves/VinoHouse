package com.VinoHouse.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BeveragePageQueryDTO implements Serializable {

    private int page;

    private int pageSize;

    private String name;

    // 分类 id
    private Integer categoryId;

    // 状态 0表示禁用 1表示启用
    private Integer status;

}
