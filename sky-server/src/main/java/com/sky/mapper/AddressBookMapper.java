package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.AddressBook;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {



    /**
     * 根据 用户id修改 是否默认地址
     * @param userId
     */

    int updateIsDefaultByUserId(Long userId);


}
