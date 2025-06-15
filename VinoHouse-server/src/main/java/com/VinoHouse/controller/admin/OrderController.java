package com.VinoHouse.controller.admin;

import com.VinoHouse.dto.OrdersCancelDTO;
import com.VinoHouse.dto.OrdersConfirmDTO;
import com.VinoHouse.dto.OrdersPageQueryDTO;
import com.VinoHouse.dto.OrdersRejectionDTO;
import com.VinoHouse.result.PageResult;
import com.VinoHouse.result.Result;
import com.VinoHouse.service.OrderService;
import com.VinoHouse.vo.OrderStatisticsVO;
import com.VinoHouse.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单管理接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单搜索
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

//    /**
//     * 各个状态的订单数量统计
//     */
//    @GetMapping("/statistics")
//    @ApiOperation("各个状态的订单数量统计")
//    public Result<OrderStatisticsVO> statistics() {
//        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
//        return Result.success(orderStatisticsVO);
//    }
//
//    /**
//     * 订单详情
//     */
//    @GetMapping("/details/{id}")
//    @ApiOperation("查询订单详情")
//    public Result<OrderVO> details(@PathVariable("id") Long id) {
//        OrderVO orderVO = orderService.details(id);
//        return Result.success(orderVO);
//    }
//
//    /**
//     * 接单
//     */
//    @PutMapping("/confirm")
//    @ApiOperation("接单")
//    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
//        orderService.confirm(ordersConfirmDTO);
//        return Result.success();
//    }
//
//    /**
//     * 拒单
//     */
//    @PutMapping("/rejection")
//    @ApiOperation("拒单")
//    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
//        orderService.rejection(ordersRejectionDTO);
//        return Result.success();
//    }
//
//    /**
//     * 取消订单
//     */
//    @PutMapping("/cancel")
//    @ApiOperation("取消订单")
//    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
//        orderService.cancel(ordersCancelDTO);
//        return Result.success();
//    }
//
//    /**
//     * 派送订单
//     */
//    @PutMapping("/delivery/{id}")
//    @ApiOperation("派送订单")
//    public Result delivery(@PathVariable("id") Long id) {
//        orderService.delivery(id);
//        return Result.success();
//    }
//
//    /**
//     * 完成订单
//     */
//    @PutMapping("/complete/{id}")
//    @ApiOperation("完成订单")
//    public Result complete(@PathVariable("id") Long id) {
//        orderService.complete(id);
//        return Result.success();
//    }
}
