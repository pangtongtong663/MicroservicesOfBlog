package com.pt.feignservice.service;

import com.pt.feignservice.dto.SignUpDto;
import com.pt.feignservice.fallback.AccountServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "account-service", fallback = AccountServiceFallback.class)
public interface AccountService {

    @RequestMapping(path = "/public/create", method = RequestMethod.POST)
    String createUser(@RequestBody SignUpDto user);

}
