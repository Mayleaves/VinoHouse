package com.VinoHouse.controller.admin;

import com.VinoHouse.dto.BeverageDTO;
import com.VinoHouse.dto.BeveragePageQueryDTO;
import com.VinoHouse.entity.Beverage;
import com.VinoHouse.result.PageResult;
import com.VinoHouse.result.Result;
import com.VinoHouse.service.BeverageService;
import com.VinoHouse.vo.BeverageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 酒水管理
 */
@RestController
@RequestMapping("/admin/dish")  // 现在这里不能修改，因为前端硬编码了 /admin/dish 这个API路径
@Api(tags = "酒水相关接口")
@Slf4j
public class BeverageController {

    @Autowired
    private BeverageService beverageService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增酒水
     * json：需要 @RequestBody
     */
    @PostMapping
    @ApiOperation("新增酒水")
    public Result save(@RequestBody BeverageDTO beverageDTO) {
        log.info("新增酒水：{}", beverageDTO);
        beverageService.saveWithFlavor(beverageDTO);

        // 清理缓存数据
        String key = "beverage_" + beverageDTO.getCategoryId();  // 指定某个酒水
        cleanCache(key);
        return Result.success();
    }

    /**
     * 酒水分页查询
     * Query：不需要 @RequestBody
     */
    @GetMapping("/page")
    @ApiOperation("酒水分页查询")
    public Result<PageResult> page(BeveragePageQueryDTO beveragePageQueryDTO) {
        log.info("酒水分页查询:{}", beveragePageQueryDTO);
        PageResult pageResult = beverageService.pageQuery(beveragePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 酒水批量删除：批量删除和单个删除共用一个接口
     */
    @DeleteMapping
    @ApiOperation("酒水批量删除")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("酒水批量删除：{}", ids);
        beverageService.deleteBatch(ids);

        // 将所有的酒水缓存数据清理掉：所有以 beverage_ 开头的 key
        cleanCache("beverage_*");

        return Result.success();
    }

    /**
     * 根据 id 查询酒水
     */
    @GetMapping("/{id}")
    @ApiOperation("根据 id 查询酒水")
    public Result<BeverageVO> getById(@PathVariable Long id) {
        log.info("根据 id 查询酒水：{}", id);
        BeverageVO beverageVO = beverageService.getByIdWithFlavor(id);
        return Result.success(beverageVO);
    }

    /**
     * 修改酒水
     */
    @PutMapping
    @ApiOperation("修改酒水")
    public Result update(@RequestBody BeverageDTO beverageDTO) {
        log.info("修改酒水：{}", beverageDTO);
        beverageService.updateWithFlavor(beverageDTO);

        // 将所有的酒水缓存数据清理掉：所有以 beverage_ 开头的 key
        cleanCache("beverage_*");

        return Result.success();
    }

    /**
     * 酒水起售、停售
     * Long id 中的 id 不能修改名字
     */
    @PostMapping("/status/{status}")
    @ApiOperation("酒水起售停售")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        beverageService.startOrStop(status, id);

        // 将所有的酒水缓存数据清理掉：所有以 beverage_ 开头的 key
        cleanCache("beverage_*");

        return Result.success();
    }

    /**
     * 根据分类 id 查询酒水
     */
    @GetMapping("/list")
    @ApiOperation("根据分类 id 查询酒水")
    public Result<List<Beverage>> list(Long categoryId) {
        List<Beverage> list = beverageService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 清理缓存数据
     * 若不清理缓存，用户端访问的数据不会更新。因为用户端访问的是 redis，不会直接访问数据库
     */
    private void cleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
