package com.sky.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.vo.DishVO;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author starlord
 * @description
 * @create 2025/6/2
 */
public interface DishService {

    /**
     * 新增菜品和对应的口味
     *
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
