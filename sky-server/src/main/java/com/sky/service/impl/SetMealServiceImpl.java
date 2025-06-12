package com.sky.service.impl;/**
 * @description
 * @author starlord
 * @create 2025/6/11
 */

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.context.BaseContext;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author starlord
 * @description
 * @create 2025/6/11
 */
@Service
@Slf4j
public class SetMealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetMealService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public Page<Setmeal> queryPage(SetmealPageQueryDTO setmealPageQueryDTO) {

        QueryWrapper<Setmeal> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(setmealPageQueryDTO.getName()), "name", setmealPageQueryDTO.getName())
                .eq(!StringUtils.isEmpty(setmealPageQueryDTO.getCategoryId()), "category_id", setmealPageQueryDTO.getCategoryId())
                .eq(!StringUtils.isEmpty(setmealPageQueryDTO.getStatus()), "status", setmealPageQueryDTO.getStatus());
        Page<Setmeal> page = new Page<>(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> setmealPage = this.baseMapper.selectPage(page, queryWrapper);

        return setmealPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setCreateTime(LocalDateTime.now());
        setmeal.setCreateUser(BaseContext.getCurrentId());
        setmeal.setUpdateTime(LocalDateTime.now());
        setmeal.setUpdateUser(BaseContext.getCurrentId());
        this.baseMapper.insert(setmeal);
        log.info("保存套餐信息：{}", setmeal);
        Long setMealId = setmeal.getId();
        if(setMealId!=null){
            List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
            setmealDishes.forEach(setmealDish -> {setmealDish.setSetmealId(setMealId);});
            setmealDishMapper.insert(setmealDishes);
        }
        return true;

    }


}
