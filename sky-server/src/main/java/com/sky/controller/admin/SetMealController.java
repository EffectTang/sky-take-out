package com.sky.controller.admin;/**
 * @description
 * @author starlord
 * @create 2025/6/10
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author starlord
 * @description
 * @create 2025/6/10
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @GetMapping("/page")
    public Result<?> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询");
        Page<Setmeal> setmeals = setMealService.queryPage(setmealPageQueryDTO);

        return Result.success(setmeals);
    }


}
