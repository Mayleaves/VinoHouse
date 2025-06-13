package com.VinoHouse.controller.user;

import com.VinoHouse.dto.OrdersPaymentDTO;
import com.VinoHouse.dto.OrdersSubmitDTO;
import com.VinoHouse.result.PageResult;
import com.VinoHouse.result.Result;
import com.VinoHouse.service.OrderService;
import com.VinoHouse.vo.OrderPaymentVO;
import com.VinoHouse.vo.OrderSubmitVO;
import com.VinoHouse.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")  // 别名
@RequestMapping("/user/order")
@Api(tags = "用户端订单相关接口")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单，参数为：{}", ordersSubmitDTO);
        OrderSubmitVO ordersSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(ordersSubmitVO);
    }

    /**
     * 订单支付
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);

        // 业务处理：修改订单状态、来单状态
        // 模拟调用微信支付接口
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        log.info("模拟交易成功：{}", ordersPaymentDTO.getOrderNumber());

        return Result.success(orderPaymentVO);
    }

//    /**
//     * 历史订单查询
//     *
//     * 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
//     */
//    @GetMapping("/historyOrders")
//    @ApiOperation("历史订单查询")
//    public Result<PageResult> page(int page, int pageSize, Integer status) {
//        PageResult pageResult = orderService.pageQuery4User(page, pageSize, status);
//        return Result.success(pageResult);
//    }
//
//    /**
//     * 查询订单详情
//     */
//    @GetMapping("/orderDetail/{id}")
//    @ApiOperation("查询订单详情")
//    public Result<OrderVO> details(@PathVariable("id") Long id) {
//        OrderVO orderVO = orderService.details(id);
//        return Result.success(orderVO);
//    }
//
//    /**
//     * 用户取消订单
//     */
//    @PutMapping("/cancel/{id}")
//    @ApiOperation("取消订单")
//    public Result cancel(@PathVariable("id") Long id) throws Exception {
//        orderService.userCancelById(id);
//        return Result.success();
//    }
//
//    /**
//     * 再来一单
//     */
//    @PostMapping("/repetition/{id}")
//    @ApiOperation("再来一单")
//    public Result repetition(@PathVariable Long id) {
//        orderService.repetition(id);
//        return Result.success();
//    }
//
//    /**
//     * 客户催单
//     */
//    @GetMapping("/reminder/{id}")
//    @ApiOperation("客户催单")
//    public Result reminder(@PathVariable("id") Long id) {
//        orderService.reminder(id);
//        return Result.success();
//    }
}
