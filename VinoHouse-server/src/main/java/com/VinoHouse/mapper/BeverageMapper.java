package com.VinoHouse.mapper;

import com.github.pagehelper.Page;
import com.VinoHouse.annotation.AutoFill;
import com.VinoHouse.dto.BeveragePageQueryDTO;
import com.VinoHouse.entity.Beverage;
import com.VinoHouse.enumeration.OperationType;
import com.VinoHouse.vo.BeverageVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BeverageMapper {

    /**
     * 根据分类 id 查询酒水数量
     */
    @Select("select count(id) from beverage where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 插入酒水数据
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Beverage beverage);

    /**
     * 酒水分页查询
     */
    Page<BeverageVO> pageQuery(BeveragePageQueryDTO beveragePageQueryDTO);

    /**
     * 根据主键查询酒水
     */
    @Select("select * from beverage where id = #{id}")
    Beverage getById(Long id);

    /**
     * 根据主键删除酒水数据
     */
    @Delete("delete from beverage where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据 id 动态修改酒水数据
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Beverage beverage);

    /**
     * 动态条件查询酒水
     */
    List<Beverage> list(Beverage beverage);

    /**
     * 根据套餐 id 查询酒水
     */
    @Select("select a.* from beverage a left join setmeal_beverage b on a.id = b.beverage_id where b.setmeal_id = #{setmealId}")
    List<Beverage> getBySetmealId(Long setmealId);

    /**
     * 根据条件统计酒水数量
     */
    Integer countByMap(Map map);
}
