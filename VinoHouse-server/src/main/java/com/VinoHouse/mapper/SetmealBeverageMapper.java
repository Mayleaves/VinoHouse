package com.VinoHouse.mapper;

import com.VinoHouse.entity.SetmealBeverage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealBeverageMapper {

    /**
     * 批量保存套餐和酒水的关联关系
     */
    void insertBatch(List<SetmealBeverage> setmealBeverages);

    /**
     * 根据套餐 id 删除套餐和酒水的关联关系
     */
    @Delete("delete from setmeal_beverage where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据酒水 id 查询对应的套餐 id
     */
    // select setmeal_id from setmeal_beverage where beverage_id in (1,2,3,4)
    List<Long> getSetmealIdsByBeverageIds(List<Long> beverageIds);

}
