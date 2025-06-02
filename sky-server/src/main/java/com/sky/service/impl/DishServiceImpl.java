package com.sky.service.impl;/**
 * @description
 * @author starlord
 * @create 2025/6/2
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author starlord
 * @description
 * @create 2025/6/2
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();

        BeanUtils.copyProperties(dishDTO, dish);
        LocalDateTime nowTime = LocalDateTime.now();
        dish.setCreateTime(nowTime);
        dish.setUpdateTime(nowTime);
        dish.setCreateUser(BaseContext.getCurrentId());
        dish.setUpdateUser(BaseContext.getCurrentId());

        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
        dishQueryWrapper.eq("name", dish.getName()).eq( "category_id", dish.getCategoryId());
        Dish resultDish = this.getOne(dishQueryWrapper);
        if( resultDish != null){
            throw new RuntimeException( "该菜品已存在，请更换名称");
        }
        boolean save = this.save(dish);
        //对名称进行判重
        if( !save){
            throw new RuntimeException( "保存菜品失败");
        }
        Dish newDish = this.getOne(dishQueryWrapper);
        //获取insert语句生成的主键值
        Long dishId = newDish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //向口味表插入n条数据
            dishFlavorMapper.insert( flavors);
        }
    }
}
