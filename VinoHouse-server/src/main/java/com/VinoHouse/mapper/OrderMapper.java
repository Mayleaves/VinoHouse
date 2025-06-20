package com.VinoHouse.mapper;

import com.github.pagehelper.Page;
import com.VinoHouse.dto.GoodsSalesDTO;
import com.VinoHouse.dto.OrdersPageQueryDTO;
import com.VinoHouse.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据
     * id="insert" parameterType="Orders"
     */
    void insert(Orders orders);

    /**
     * 根据订单号和用户 id 查询订单
     */
    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 修改订单信息
     */
    void update(Orders orders);

    /**
     * 分页条件查询并按下单时间排序
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据 id 查询订单
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 根据状态统计订单数量
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 根据订单状态和下单时间查询订单
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 根据动态条件统计营业额数据
     */
    Double sumByMap(Map map);

    /**
     * 根据动态条件统计订单数量
     */
    Integer countByMap(Map map);

    /**
     * 统计指定时间区间内的销量排名前 10
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}
