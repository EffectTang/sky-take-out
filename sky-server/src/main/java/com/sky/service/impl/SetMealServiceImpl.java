package com.sky.service.impl;/**
 * @description
 * @author starlord
 * @create 2025/6/11
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author starlord
 * @description
 * @create 2025/6/11
 */
@Service
public class SetMealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetMealService {

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
}
