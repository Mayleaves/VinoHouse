package com.VinoHouse.mapper;

import com.VinoHouse.annotation.AutoFill;
import com.VinoHouse.dto.SetmealPageQueryDTO;
import com.VinoHouse.entity.Setmeal;
import com.VinoHouse.enumeration.OperationType;
import com.VinoHouse.vo.BeverageItemVO;
import com.VinoHouse.vo.SetmealVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类 id 询套餐的数量
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据 id 修改套餐
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 新增套餐
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 分页查询
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据 id 查询套餐
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 根据 id 删除套餐
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long setmealId);

    /**
     * 根据 id 查询套餐和套餐酒水关系
     */
    SetmealVO getByIdWithBeverage(Long id);

    /**
     * 动态条件查询套餐
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐 id 查询酒水选项
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_beverage sd left join beverage d on sd.beverage_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<BeverageItemVO> getBeverageItemBySetmealId(Long setmealId);
//
//    /**
//     * 根据条件统计套餐数量
//     */
//    Integer countByMap(Map map);
}
