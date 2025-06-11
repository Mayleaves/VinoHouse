package com.VinoHouse.mapper;

import com.VinoHouse.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 动态条件查询
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据 id 修改商品数量
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插入购物车数据
     */
    @Insert("insert into shopping_cart (name, user_id, beverage_id, setmeal_id, beverage_flavor, number, amount, image, create_time) " +
            " values (#{name},#{userId},#{beverageId},#{setmealId},#{beverageFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户 id 删除购物车数据
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    /**
     * 根据 id 删除购物车数据
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);

    /**
     * 批量插入购物车数据
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
