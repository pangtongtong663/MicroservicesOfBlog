package com.pt.accountservice.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pt.accountservice.po.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
