package com.VinoHouse.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 酒水总览
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeverageOverViewVO implements Serializable {

    // 已启售数量
    private Integer sold;

    // 已停售数量
    private Integer discontinued;
}
