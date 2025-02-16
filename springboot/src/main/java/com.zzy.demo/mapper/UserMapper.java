package com.zzy.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzy.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
