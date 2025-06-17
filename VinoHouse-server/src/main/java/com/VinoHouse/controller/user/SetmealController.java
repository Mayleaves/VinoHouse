package com.VinoHouse.controller.user;

import com.VinoHouse.constant.StatusConstant;
import com.VinoHouse.entity.Setmeal;
import com.VinoHouse.result.Result;
import com.VinoHouse.service.SetmealService;
import com.VinoHouse.vo.BeverageItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C端-套餐浏览接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类 id 查询套餐
     */
    @GetMapping("/list")
    @ApiOperation("根据分类 id 查询套餐")
    @Cacheable(cacheNames = "setmealCache", key = "#categoryId")  // key: setmealCache::100
    public Result<List<Setmeal>> list(Long categoryId) {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);

        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }

    /**
     * 根据套餐 id 查询包含的酒水列表
     */
    @GetMapping("/dish/{id}")  // 现在这里不能修改，因为前端硬编码了 /dish/{id} 这个API路径
    @ApiOperation("根据套餐 id 查询包含的酒水列表")
    public Result<List<BeverageItemVO>> beverageList(@PathVariable("id") Long id) {
        List<BeverageItemVO> list = setmealService.getBeverageItemById(id);
        return Result.success(list);
    }
}
