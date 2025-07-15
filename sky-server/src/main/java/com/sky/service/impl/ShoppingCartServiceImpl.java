package com.sky.service.impl;/**
 * @description
 * @author starlord
 * @create 2025/7/15
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.DishService;
import com.sky.service.SetMealService;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author starlord
 * @description
 * @create 2025/7/15
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setMealMapper;

    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车：{}", shoppingCartDTO);
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCart.setNumber(1);

        if(shoppingCart.getDishId()!=null){
            QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dish_id", shoppingCart.getDishId())
                    .eq("user_id", shoppingCart.getUserId())
                    .eq("dish_flavor", shoppingCart.getDishFlavor());
            ShoppingCart shoppingCartDish = this.getBaseMapper().selectOne(queryWrapper);
            if(shoppingCartDish!=null){
                shoppingCartDish.setNumber(shoppingCartDish.getNumber()+1);
                this.updateById(shoppingCartDish);
            }else {
                Dish dish = dishMapper.selectById(shoppingCart.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                this.save(shoppingCart);
            }

        }else {
            QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("setmeal_id", shoppingCart.getSetmealId())
                    .eq("user_id", shoppingCart.getUserId());
            ShoppingCart shoppingCartMeal = this.getBaseMapper().selectOne(queryWrapper);
            if(shoppingCartMeal==null){
                Setmeal setmeal = setMealMapper.selectById(shoppingCart.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
                this.save(shoppingCart);
            }else {
                shoppingCartMeal.setNumber(shoppingCartMeal.getNumber()+1);
                this.updateById(shoppingCartMeal);
            }


        }

    }

    @Override
    public List showShoppingCart() {
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = this.list(queryWrapper);
        return shoppingCartList;
    }
}
