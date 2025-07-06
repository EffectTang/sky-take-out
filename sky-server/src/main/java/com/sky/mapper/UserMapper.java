package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author starlord
 * @description
 * @create 2025/7/2
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
