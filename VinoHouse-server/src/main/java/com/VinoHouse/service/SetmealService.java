package com.VinoHouse.service;

import com.VinoHouse.dto.SetmealDTO;
import com.VinoHouse.dto.SetmealPageQueryDTO;
import com.VinoHouse.entity.Setmeal;
import com.VinoHouse.result.PageResult;
import com.VinoHouse.vo.BeverageItemVO;
import com.VinoHouse.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐，同时需要保存套餐和酒水的关联关系
     */
    void saveWithBeverage(SetmealDTO setmealDTO);

    /**
     * 分页查询
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据 id 查询套餐和关联的酒水数据
     */
    SetmealVO getByIdWithBeverage(Long id);

    /**
     * 修改套餐
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 套餐起售、停售
     */
    void startOrStop(Integer status, Long id);

    /**
     * 条件查询
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据 id 查询酒水选项
     */
    List<BeverageItemVO> getBeverageItemById(Long id);
}
