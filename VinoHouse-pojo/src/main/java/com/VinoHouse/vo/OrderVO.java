package com.VinoHouse.vo;

import com.VinoHouse.entity.OrderDetail;
import com.VinoHouse.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {

    // 订单酒水信息
    private String orderDishes;  // 前端硬编码，因此不能修改为 orderBeverages

    // 订单详情
    private List<OrderDetail> orderDetailList;

}
