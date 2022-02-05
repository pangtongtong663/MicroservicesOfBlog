package com.pt.accountservice.service;

import com.pt.accountservice.dto.SignUpDto;
import com.pt.accountservice.po.User;

public interface AccountService {
    User getUserByName(String username);

    Integer getUserIdByName(String username);

    Integer getUserLikesByName(String username);

    Integer getUserPageViewsByName(String username);

    void create(SignUpDto userDto);

}
