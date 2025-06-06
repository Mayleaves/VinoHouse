package com.VinoHouse.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 酒水口味
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeverageFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 酒水id
    private Long beverageId;

    // 口味名称
    private String name;

    // 口味数据list
    private String value;

}
