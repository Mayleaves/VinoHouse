package com.VinoHouse.service.impl;

import com.VinoHouse.constant.StatusConstant;
import com.VinoHouse.entity.Orders;
import com.VinoHouse.mapper.BeverageMapper;
import com.VinoHouse.mapper.OrderMapper;
import com.VinoHouse.mapper.SetmealMapper;
import com.VinoHouse.mapper.UserMapper;
import com.VinoHouse.service.WorkspaceService;
import com.VinoHouse.vo.BusinessDataVO;
import com.VinoHouse.vo.BeverageOverViewVO;
import com.VinoHouse.vo.OrderOverViewVO;
import com.VinoHouse.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BeverageMapper beverageMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 根据时间段统计营业数据
     *
     * 营业额：当日已完成订单的总金额
     * 有效订单：当日已完成订单的数量
     * 平均客单价：营业额 / 有效订单数
     * 订单完成率：有效订单数 / 总订单数
     * 新增用户：当日新增用户的数量
     */
    public BusinessDataVO getBusinessData(LocalDateTime beginTime, LocalDateTime endTime) {

        Map map = new HashMap();
        map.put("begin", beginTime);
        map.put("end", endTime);

        // 查询总订单数
        Integer totalOrderCount = orderMapper.countByMap(map);

        map.put("status", Orders.COMPLETED);
        // 营业额
        Double turnover = orderMapper.sumByMap(map);
        // turnover = turnover == null? 0.0 : turnover;  // 已合并在 sql 语法

        // 有效订单数
        Integer validOrderCount = orderMapper.countByMap(map);

        Double unitPrice = 0.0;  // 平均客单价
        Double orderCompletionRate = 0.0;  // 订单完成率
        if(totalOrderCount != 0 && validOrderCount != 0){
            unitPrice = turnover / validOrderCount;
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }

        // 新增用户数
        Integer newUsers = userMapper.countByMap(map);

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }

    /**
     * 查询订单管理数据
     */
    public OrderOverViewVO getOrderOverView() {
        Map map = new HashMap();
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));

        // 待接单
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingOrders = orderMapper.countByMap(map);

        // 待派送
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countByMap(map);

        // 已完成
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByMap(map);

        // 已取消
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countByMap(map);

        // 全部订单
        map.put("status", null);
        Integer allOrders = orderMapper.countByMap(map);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * 查询酒水总览
     */
    public BeverageOverViewVO getBeverageOverView() {
        Map map = new HashMap();

        map.put("status", StatusConstant.ENABLE);  // 启用
        Integer sold = beverageMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);  //禁用
        Integer discontinued = beverageMapper.countByMap(map);

        return BeverageOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 查询套餐总览
     */
    public SetmealOverViewVO getSetmealOverView() {
        Map map = new HashMap();

        map.put("status", StatusConstant.ENABLE);  // 启用
        Integer sold = setmealMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);  //禁用
        Integer discontinued = setmealMapper.countByMap(map);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
