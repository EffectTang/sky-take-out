package com.sky.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * @author starlord
 * @description
 * @create 2025/6/11
 */
public interface SetMealService {
    Page<SetmealVO> queryPage(SetmealPageQueryDTO setmealPageQueryDTO);

    boolean save(SetmealDTO setmealDTO);

    boolean startOrStop(Integer status, Long id);

    SetmealVO getInfoById(Long id);

    boolean update(SetmealDTO setmealDTO);
}
