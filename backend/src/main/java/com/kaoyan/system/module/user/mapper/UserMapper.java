package com.kaoyan.system.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kaoyan.system.module.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
