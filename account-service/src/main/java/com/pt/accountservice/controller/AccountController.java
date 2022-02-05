package com.pt.accountservice.controller;

import com.pt.accountservice.dto.SignUpDto;
import com.pt.accountservice.po.User;
import com.pt.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @RequestMapping(path = "/account/current", method = RequestMethod.GET)
    public User getCurrentUser(Principal principal) {
        return accountService.getUserByName(principal.getName());
    }

    @RequestMapping(path = "/public/create", method = RequestMethod.POST)
    public String createUser(@RequestBody SignUpDto user) {
        accountService.create(user);
        return "创建成功！";
    }

    @RequestMapping(path = "/account/name", method = RequestMethod.GET)
    public String getCurrentUsername(Principal principal) {
        return principal.getName();
    }

    @RequestMapping(path = "/account/likes", method = RequestMethod.GET)
    public Integer getCurrentLikes(Principal principal) {
        return accountService.getUserLikesByName(principal.getName());
    }

    @RequestMapping(path = "/account/pageviews", method = RequestMethod.GET)
    public Integer getCurrentPageviews(Principal principal) {
        return accountService.getUserPageViewsByName(principal.getName());
    }


}
