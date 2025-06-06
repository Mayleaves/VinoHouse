package com.VinoHouse.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    private Long beverageId;
    private Long setmealId;
    private String beverageFlavor;

}
