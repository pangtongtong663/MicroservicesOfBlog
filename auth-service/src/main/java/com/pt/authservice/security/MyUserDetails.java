package com.pt.authservice.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pt.authservice.dao.UserMapper;
import com.pt.authservice.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class MyUserDetails implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", username);
        if (userMapper.selectList(qw).size() != 1) {
            throw new UsernameNotFoundException(username);
        }
        User user = userMapper.selectList(qw).get(0);
        logger.info("表单登录用户名: " + username);
        String password = user.getPassword();
        logger.info("密码是: " + password);
        return user;
    }
}
