package com.pt.accountservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pt.accountservice.dto.SignUpDto;
import com.pt.accountservice.dao.UserMapper;
import com.pt.accountservice.exception.DuplicateInfoException;
import com.pt.accountservice.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserMapper userMapper;

    @Override
    public Integer getUserIdByName(String username) {
        User user = getUserByName(username);
        if (user.getUsername().equals("error")) {
            return -1;
        } else {
            return user.getId();
        }
    }

    @Override
    public Integer getUserLikesByName(String username) {
        User user = getUserByName(username);
        if (user.getUsername().equals("error")) {
            return -1;
        } else {
            return user.getLikes();
        }
    }

    @Override
    public Integer getUserPageViewsByName(String username) {
        User user = getUserByName(username);
        if (user.getUsername().equals("error")) {
            return -1;
        } else {
            return user.getPageViews();
        }
    }

    @Override
    public User getUserByName(String username) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", username);
        if (userMapper.selectList(qw).size() != 1) {
            return User.builder().username("error").password("123").pageViews(0).likes(0).build();
        }
        return userMapper.selectList(qw).get(0);
    }

    @Override
    public void create(SignUpDto userDto) {
        DuplicateInfoException e = new DuplicateInfoException();
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username", userDto.getUsername());
        if (userMapper.selectList(qw).size() != 0) {
            e.addDuplicateInfoField("username");
        }
        String password = encoder.encode(userDto.getPassword());
        User user = User.builder().username(userDto.getUsername())
                    .password(password)
                    .likes(0).pageViews(0)
                    .build();
            userMapper.insert(user);
            logger.info("User has been saved");
        }

    }
