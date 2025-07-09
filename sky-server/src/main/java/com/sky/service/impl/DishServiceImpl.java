package com.sky.service.impl;/**
 * @description
 * @author starlord
 * @create 2025/6/2
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.*;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private SetmealDishMapper setmealDishMapper;

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


    @Override
    public Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        Page<DishVO> page = new Page<>(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        //后绪步骤实现
        return this.baseMapper.pageQuery(dishPageQueryDTO,page);
    }

    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        //判断当前菜品是否能够删除---是否存在起售中的菜品？？
        for (Long id:ids) {
            Dish dish = this.getById(id);
            if( dish!=null && Objects.equals(dish.getStatus(), StatusConstant.ENABLE)){
                //当前菜品处于起售中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //判断当前菜品是否能够删除---是否被套餐关联了？？
        List<Long> setmealIds = this.setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(  setmealIds != null && setmealIds.size() > 0){
            //当前菜品被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品表中的菜品数据
        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("dish_id", ids);
        int deleteResult = this.dishFlavorMapper.delete(queryWrapper);
        if (deleteResult <= 0) {
            log.warn("未能删除口味数据，可能数据不存在或已被删除");
        }
        boolean deleteDishResult = this.removeByIds(ids);

    }

    @Override
    public DishVO getByIdWithFlavor(Long id) {

        //根据id查询菜品数据
        Dish dish = this.getById(id);

        //根据菜品id查询口味数据
        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dish_id", id);
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(queryWrapper);
        //将查询到的数据封装到VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {

        //修改菜品表基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setUpdateTime(LocalDateTime.now());
        dish.setUpdateUser(BaseContext.getCurrentId());
        this.updateById(dish);


        //删除原有的口味数据
        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dish_id", dishDTO.getId());
        dishFlavorMapper.delete(queryWrapper);

        //重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            //向口味表插入n条数据
            dishFlavorMapper.insert( flavors);
        }

    }

    @Override
    public void startOrStop(Integer status, Long id) {

        Dish dish = this.getById(id);

        dish.setStatus(status);
        dish.setUpdateTime(LocalDateTime.now());
        dish.setUpdateUser(BaseContext.getCurrentId());
        this.updateById(dish);

    }

    @Override
    public List<Dish> listByCategoryId(Long id) {
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", id).eq("status", StatusConstant.ENABLE);
        return this.list(queryWrapper);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        //会根据 dish 对象中不为 null 的属性，默认生成等值匹配（eq）的查询条
        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
        dishQueryWrapper.eq(dish.getCategoryId() != null, "category_id", dish.getCategoryId())
                .eq(dish.getStatus() != null, "status", dish.getStatus())
                .like(dish.getName() != null, "name", dish.getName())
                .orderByDesc("create_time");
        List<Dish> dishList = this.list(dishQueryWrapper);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            //List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());
            QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dish_id",d.getId());
            List<DishFlavor> flavors = dishFlavorMapper.selectList(queryWrapper);

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
