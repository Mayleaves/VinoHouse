package com.VinoHouse.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeverageItemVO implements Serializable {

    // 酒水名称
    private String name;

    // 份数
    private Integer copies;

    // 酒水图片
    private String image;

    // 酒水描述
    private String description;
}
