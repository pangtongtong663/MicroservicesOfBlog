package com.pt.blogservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pt.blogservice.po.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
