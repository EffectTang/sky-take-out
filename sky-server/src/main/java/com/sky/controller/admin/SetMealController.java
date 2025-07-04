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
import org.springframework.web.bind.annotation.*;

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
        Page<SetmealVO> setmeals = setMealService.queryPage(setmealPageQueryDTO);
        return Result.success(setmeals);
    }

    @PostMapping
    public Result<?> save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐");
        setMealService.save(setmealDTO);
        return Result.success("新增成功");
    }

    // /admin/setmeal/status/1  status/1?id=1
    @PostMapping("/status/{status}")
    public Result<?> startOrStop(@PathVariable Integer status,@RequestParam Long id) {
        log.info("起售或停售套餐");
        boolean falg = setMealService.startOrStop(status, id);
        return falg ? Result.success("起售或停售成功") : Result.error("起售或停售失败");
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据id查询套餐");
        SetmealVO setmealVO = setMealService.getInfoById(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    public Result<?> update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐");
        //return null;
        boolean falg = setMealService.update(setmealDTO);
        return falg ? Result.success("起售或停售成功") : Result.error("起售或停售失败");
    }

    @DeleteMapping
    public Result<?> delete(@RequestParam List<Long> ids) {
        log.info("批量删除套餐");
        setMealService.deleteByIds(ids);
        return Result.success("删除成功");
    }


}
