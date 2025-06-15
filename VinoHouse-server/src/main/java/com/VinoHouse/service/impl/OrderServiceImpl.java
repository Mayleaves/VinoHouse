package com.VinoHouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.VinoHouse.constant.MessageConstant;
import com.VinoHouse.context.BaseContext;
import com.VinoHouse.dto.*;
import com.VinoHouse.entity.*;
import com.VinoHouse.exception.AddressBookBusinessException;
import com.VinoHouse.exception.OrderBusinessException;
import com.VinoHouse.exception.ShoppingCartBusinessException;
import com.VinoHouse.mapper.*;
import com.VinoHouse.result.PageResult;
import com.VinoHouse.service.OrderService;
import com.VinoHouse.utils.HttpClientUtil;
import com.VinoHouse.utils.WeChatPayUtil;
import com.VinoHouse.vo.OrderPaymentVO;
import com.VinoHouse.vo.OrderStatisticsVO;
import com.VinoHouse.vo.OrderSubmitVO;
import com.VinoHouse.vo.OrderVO;
//import com.VinoHouse.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
//    @Autowired
//    private WebSocketServer webSocketServer;

    /**
     * 用户下单
     */
    @Transactional  // 事务注解：原子性
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 1. 异常情况的处理
        // 1.1 检查收货地址是否为空
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        // 1.2 检查用户地址是否超过配送范围
//        checkOutOfRange(addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());
        // 1.3 检查当前用户的购物车数据
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 2. 构造订单数据
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(userId);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());

        // 向订单表插入 1 条数据
        orderMapper.insert(order);

        // 3. 订单明细数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();  // 订单明细
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());  // 设置当前订单明细关联的订单 id
            orderDetailList.add(orderDetail);
        }

        // 向订单明细表插入 n 条数据
        orderDetailMapper.insertBatch(orderDetailList);

        // 4. 清理购物车中的数据
        shoppingCartMapper.deleteByUserId(userId);

        // 5. 封装返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();

        return orderSubmitVO;
    }

    /**
     * 订单支付
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {

        /*// 1. 真实调用微信支付
        // 当前登录用户 id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);
         // 调用微信支付接口，生成预支付交易单
         JSONObject jsonObject = weChatPayUtil.pay(
                 ordersPaymentDTO.getOrderNumber(),  // 商户订单号
                 new BigDecimal(0.01), // 支付金额，单位：元
                 "万酒屋外卖订单",  // 商品描述
                 user.getOpenid()  // 微信用户的 openid
         );*/
        // 2. 模拟调用微信支付
        // 生成空 JSON，跳过微信支付流程
        JSONObject jsonObject = new JSONObject();

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     */
    public void paySuccess(String outTradeNo) {
        // 当前登录用户 id
        Long userId = BaseContext.getCurrentId();
        // 据订单号查询订单
        Orders ordersDB = orderMapper.getByNumberAndUserId(outTradeNo, userId);

        // 根据订单 id 更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
//        // 通过 WebSocket 向客户端浏览器推送消息 type orderId content
//        Map map = new HashMap();
//        map.put("type", 1);
//        // 1表示来单提醒 2表示客户催单
//        map.put("orderId", ordersDB.getId());
//        map.put("content", "订单号" + outTradeNo);
//
//        String json = JSON.toJSONString(map);
//        webSocketServer.sendToAllClient(json);
    }

    /**
     * 用户端订单分页查询
     */
    public PageResult pageQuery4User(int pageNum, int pageSize, Integer status) {
        // 设置分页
        PageHelper.startPage(pageNum, pageSize);

        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);

        // 分页条件查询
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> list = new ArrayList();

        // 查询出订单明细，并封装入 OrderVO 进行响应
        if (page != null && page.getTotal() > 0) {
            for (Orders orders : page) {
                Long orderId = orders.getId();  // 订单 id

                // 查询订单明细
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orderId);

                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetails);

                list.add(orderVO);
            }
        }
        return new PageResult(page.getTotal(), list);
    }

    /**
     * 查询订单详情
     */
    public OrderVO details(Long id) {
        // 根据 id 查询订单
        Orders orders = orderMapper.getById(id);

        // 查询该订单对应的酒水/套餐明细
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将该订单及其详情封装到 OrderVO 并返回
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * 用户取消订单
     */
    public void userCancelById(Long id) throws Exception{
        // 根据 id 查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // 订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (ordersDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());

        // 订单处于待接单状态下取消，需要进行退款
        if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            // 调用微信支付退款接口
//             weChatPayUtil.refund(
//                     ordersDB.getNumber(),  // 商户订单号
//                     ordersDB.getNumber(),  // 商户退款单号
//                     new BigDecimal(0.01),  // 退款金额，单位 元
//                     new BigDecimal(0.01)); // 原订单金额

            // 支付状态修改为退款
            orders.setPayStatus(Orders.REFUND);
        }

        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 再来一单
     */
    public void repetition(Long id) {
        // 查询当前用户 id
        Long userId = BaseContext.getCurrentId();

        // 根据订单 id 查询当前订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);

        // 将订单详情对象转换为购物车对象
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();

            // 将原订单详情里面的酒水信息重新复制到购物车对象中
            BeanUtils.copyProperties(x, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());

            return shoppingCart;
        }).collect(Collectors.toList());

        // 将购物车对象批量添加到数据库
        shoppingCartMapper.insertBatch(shoppingCartList);
    }

    /**
     * 订单搜索
     */
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());

        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        // 部分订单状态，需要额外返回订单酒水信息，将 Orders 转化为 OrderVO
        List<OrderVO> orderVOList = getOrderVOList(page);

        return new PageResult(page.getTotal(), orderVOList);
    }

    /**
     * 获取视图对象(OrderVO)列表
     */
    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        // 需要返回订单酒水信息，自定义 OrderVO 响应结果
        List<OrderVO> orderVOList = new ArrayList<>();

        List<Orders> ordersList = page.getResult();
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                // 将共同字段复制到 OrderVO
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                String orderBeverages = getOrderBeveragesStr(orders);

                // 将订单酒水信息封装到 orderVO 中，并添加到 orderVOList
                orderVO.setOrderDishes(orderBeverages);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }

    /**
     * 根据订单 id 获取酒水信息字符串
     */
    private String getOrderBeveragesStr(Orders orders) {
        // 查询订单酒水详情信息（订单中的酒水和数量）
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将每一条订单酒水信息拼接为字符串（格式：马天尼*3;）
        List<String> orderBeverageList = orderDetailList.stream().map(x -> {
            String orderBeverage = x.getName() + "*" + x.getNumber() + ";";
            return orderBeverage;
        }).collect(Collectors.toList());

        // 将该订单对应的所有酒水信息拼接在一起
        return String.join("", orderBeverageList);
    }

    /**
     * 各个状态的订单数量统计
     */
    public OrderStatisticsVO statistics() {
        // 根据状态，分别查询出待接单、待派送、派送中的订单数量
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);

        // 将查询出的数据封装到 orderStatisticsVO 中响应
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        return orderStatisticsVO;
    }

    /**
     * 接单
     */
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();

        orderMapper.update(orders);
    }

    /**
     * 拒单
     */
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        // 根据 id 查询订单
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());

        // 订单只有存在且状态为 2（待接单）才可以拒单
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 支付状态
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == Orders.PAID) {
            // 用户已支付，需要退款
//             String refund = weChatPayUtil.refund(
//                     ordersDB.getNumber(),
//                     ordersDB.getNumber(),
//                     new BigDecimal(0.01),
//                     new BigDecimal(0.01));
//             log.info("申请退款：{}", refund);
        }

        // 拒单需要退款，根据订单 id 更新订单状态、拒单原因、取消时间
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * 取消订单
     */
    public void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception {
        // 根据 id 查询订单
        Orders ordersDB = orderMapper.getById(ordersCancelDTO.getId());

        // 支付状态
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == 1) {
            // 用户已支付，需要退款
//             String refund = weChatPayUtil.refund(
//                     ordersDB.getNumber(),
//                     ordersDB.getNumber(),
//                     new BigDecimal(0.01),
//                     new BigDecimal(0.01));
//             log.info("申请退款：{}", refund);
        }

        // 管理端取消订单需要退款，根据订单id更新订单状态、取消原因、取消时间
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 派送订单
     */
    public void delivery(Long id) {
        // 根据 id 查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在，并且状态为 3
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为派送中
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);

        orderMapper.update(orders);
    }

    /**
     * 完成订单
     */
    public void complete(Long id) {
        // 根据 id 查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在，并且状态为 4
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态，状态转为完成
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * 客户催单
     */
    public void reminder(Long id) {
        // 根据 id 查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Map map = new HashMap();
        map.put("type", 2);
        // 1来单提醒 2客户接单
        map.put("orderId", id);
        map.put("content", "订单号:" + ordersDB.getNumber());
        // 通过 websocke 向客户端浏览器来推送消息
//        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }

//    @Value("${VinoHouse.shop.address}")
//    private String shopAddress;
//
//    @Value("${VinoHouse.baidu.ak}")
//    private String ak;
//
//    /**
//     * 检查客户的收货地址是否超出配送范围
//     */
//    private void checkOutOfRange(String address) {
//        Map map = new HashMap();
//        map.put("address", shopAddress);
//        map.put("output", "json");
//        map.put("ak", ak);
//
//        // 获取店铺的经纬度坐标
//        String shopCoordinate = HttpClientUtil.doGet("https:// api.map.baidu.com/geocoding/v3", map);
//
//        JSONObject jsonObject = JSON.parseObject(shopCoordinate);
//        if (!jsonObject.getString("status").equals("0")) {
//            throw new OrderBusinessException("店铺地址解析失败");
//        }
//
//        // 数据解析
//        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
//        String lat = location.getString("lat");
//        String lng = location.getString("lng");
//        // 店铺经纬度坐标
//        String shopLngLat = lat + "," + lng;
//
//        map.put("address", address);
//        // 获取用户收货地址的经纬度坐标
//        String userCoordinate = HttpClientUtil.doGet("https:// api.map.baidu.com/geocoding/v3", map);
//
//        jsonObject = JSON.parseObject(userCoordinate);
//        if (!jsonObject.getString("status").equals("0")) {
//            throw new OrderBusinessException("收货地址解析失败");
//        }
//
//        // 数据解析
//        location = jsonObject.getJSONObject("result").getJSONObject("location");
//        lat = location.getString("lat");
//        lng = location.getString("lng");
//        // 用户收货地址经纬度坐标
//        String userLngLat = lat + "," + lng;
//
//        map.put("origin", shopLngLat);
//        map.put("destination", userLngLat);
//        map.put("steps_info", "0");
//
//        // 路线规划
//        String json = HttpClientUtil.doGet("https:// api.map.baidu.com/directionlite/v1/driving", map);
//
//        jsonObject = JSON.parseObject(json);
//        if (!jsonObject.getString("status").equals("0")) {
//            throw new OrderBusinessException("配送路线规划失败");
//        }
//
//        // 数据解析
//        JSONObject result = jsonObject.getJSONObject("result");
//        JSONArray jsonArray = (JSONArray) result.get("routes");
//        Integer distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");
//
//        if (distance > 5000) {
//            // 配送距离超过5000米
//            throw new OrderBusinessException("超出配送范围");
//        }
//    }
}