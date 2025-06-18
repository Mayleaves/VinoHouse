package com.VinoHouse.service;

import com.VinoHouse.vo.BusinessDataVO;
import com.VinoHouse.vo.BeverageOverViewVO;
import com.VinoHouse.vo.OrderOverViewVO;
import com.VinoHouse.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {

    /**
     * 根据时间段统计营业数据
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 查询订单管理数据
     */
    OrderOverViewVO getOrderOverView();

    /**
     * 查询酒水总览
     */
    BeverageOverViewVO getBeverageOverView();

    /**
     * 查询套餐总览
     */
    SetmealOverViewVO getSetmealOverView();

}
