package com.VinoHouse.mapper;

import com.VinoHouse.entity.BeverageFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BeverageFlavorMapper {
    
    /**
     * 批量插入口味数据
     */
    void insertBatch(List<BeverageFlavor> flavors);

    /**
     * 根据酒水 id 删除对应的口味数据
     */
    @Delete("delete from beverage_flavor where beverage_id = #{beverageId}")
    void deleteByBeverageId(Long beverageId);

    /**
     * 根据酒水 id 查询对应的口味数据
     */
    @Select("select * from beverage_flavor where beverage_id = #{beverageId}")
    List<BeverageFlavor> getByBeverageId(Long beverageId);
}
