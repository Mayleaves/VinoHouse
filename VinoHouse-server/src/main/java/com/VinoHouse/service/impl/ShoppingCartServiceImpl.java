package com.VinoHouse.service.impl;

import com.VinoHouse.context.BaseContext;
import com.VinoHouse.dto.ShoppingCartDTO;
import com.VinoHouse.entity.Beverage;
import com.VinoHouse.entity.Setmeal;
import com.VinoHouse.entity.ShoppingCart;
import com.VinoHouse.mapper.BeverageMapper;
import com.VinoHouse.mapper.SetmealMapper;
import com.VinoHouse.mapper.ShoppingCartMapper;
import com.VinoHouse.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private BeverageMapper beverageMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 判断当前加入到购物车中的商品是否已经存在了
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        // 因为 shoppingCartDTO 和 shoppingCart 对应的酒水字段不一样，copyProperties 无法复制过来，因此这里需要重新设置一下
        shoppingCart.setBeverageId(shoppingCartDTO.getDishId());
        shoppingCart.setBeverageFlavor(shoppingCartDTO.getDishFlavor());
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        // 如果已经存在了，只需要将数量加一
        if(list != null && list.size() > 0){
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);  // update shopping_cart set number = ? where id = ?
            shoppingCartMapper.updateNumberById(cart);
        }else {
            // 如果不存在，需要插入一条购物车数据
            // 判断本次添加到购物车的是酒水还是套餐
            Long beverageId = shoppingCartDTO.getDishId();
            if(beverageId != null){
                // 本次添加到购物车的是酒水
                Beverage beverage = beverageMapper.getById(beverageId);
                shoppingCart.setName(beverage.getName());
                shoppingCart.setImage(beverage.getImage());
                shoppingCart.setAmount(beverage.getPrice());
            }else{
                // 本次添加到购物车的是套餐
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     */
    public List<ShoppingCart> showShoppingCart() {
        // 获取到当前微信用户的 id
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     */
    public void cleanShoppingCart() {
        // 获取到当前微信用户的 id
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    /**
     * 删除购物车中一个商品
     */
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        // 设置查询条件，查询当前登录用户的购物车数据
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        if(list != null && list.size() > 0){
            shoppingCart = list.get(0);

            Integer number = shoppingCart.getNumber();
            if(number == 1){
                // 当前商品在购物车中的份数为 1，直接删除当前记录
                shoppingCartMapper.deleteById(shoppingCart.getId());
            }else {
                // 当前商品在购物车中的份数不为 1，修改份数即可
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }
    }
}
