package com.VinoHouse.controller.user;

import com.VinoHouse.constant.StatusConstant;
import com.VinoHouse.entity.Beverage;
import com.VinoHouse.result.Result;
import com.VinoHouse.service.BeverageService;
import com.VinoHouse.vo.BeverageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userBeverageController")  // 指定 Bean 名称
@RequestMapping("/user/dish")  // 现在这里不能修改，因为前端硬编码了 /user/beverage 这个API路径
@Slf4j
@Api(tags = "C端-酒水浏览接口")
public class BeverageController {

    @Autowired
    private BeverageService beverageService;

    /**
     * 根据分类 id 查询酒水
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询酒水")
    public Result<List<BeverageVO>> list(Long categoryId) {
        Beverage beverage = new Beverage();
        beverage.setCategoryId(categoryId);
        beverage.setStatus(StatusConstant.ENABLE);  // 查询起售中的酒水

        List<BeverageVO> list = beverageService.listWithFlavor(beverage);

        return Result.success(list);
    }

}
