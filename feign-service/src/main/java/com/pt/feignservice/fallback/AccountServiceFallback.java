package com.pt.feignservice.fallback;

import com.pt.feignservice.dto.SignUpDto;
import com.pt.feignservice.service.AccountService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class AccountServiceFallback implements AccountService {

    @Override
    public String createUser(@RequestBody SignUpDto user) {
        return "创建失败！";
    }



}
