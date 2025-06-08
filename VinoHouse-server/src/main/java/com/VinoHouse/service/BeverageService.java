package com.VinoHouse.service;

import com.VinoHouse.dto.BeverageDTO;
import com.VinoHouse.dto.BeveragePageQueryDTO;
import com.VinoHouse.entity.Beverage;
import com.VinoHouse.result.PageResult;
import com.VinoHouse.vo.BeverageVO;

import java.util.List;

public interface BeverageService {

    /**
     * 新增酒水和对应的口味
     */
    public void saveWithFlavor(BeverageDTO beverageDTO);

    /**
     * 酒水分页查询
     */
    PageResult pageQuery(BeveragePageQueryDTO beveragePageQueryDTO);

    /**
     * 酒水批量删除
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据 id 查询酒水和对应的口味数据
     */
    BeverageVO getByIdWithFlavor(Long id);

    /**
     * 根据 id 修改酒水基本信息和对应的口味信息
     */
    void updateWithFlavor(BeverageDTO beverageDTO);

    /**
     * 酒水起售、停售
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据分类 id 查询酒水
     */
    List<Beverage> list(Long categoryId);

    /**
     * 条件查询酒水和口味
     */
    List<BeverageVO> listWithFlavor(Beverage beverage);
}