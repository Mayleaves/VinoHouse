package com.VinoHouse.controller.user;

import com.VinoHouse.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";
    public static final String PHONE = "13336666999";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取店铺的营业状态
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取到店铺的营业状态为：{}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }

    /**
     * 获取店铺电话
     * 前端没写，仅能通过接口文档测试
     */
    @GetMapping("/phone")
    @ApiOperation("获取店铺电话")
    public Result<String> getShopPhone(){
        log.info("获取店铺电话为：{}", PHONE);
        return Result.success(PHONE);
    }
}
