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
public class SalesTop10ReportVO implements Serializable {

    // 商品名称列表，以逗号分隔，例如：鸡尾酒,红酒,伏特加
    private String nameList;

    // 销量列表，以逗号分隔，例如：260,215,200
    private String numberList;

}
