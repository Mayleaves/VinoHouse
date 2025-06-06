package com.VinoHouse.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BeverageMapper {

    /**
     * 根据分类 id 查询酒水数量
     */
    @Select("select count(id) from beverage where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

}
