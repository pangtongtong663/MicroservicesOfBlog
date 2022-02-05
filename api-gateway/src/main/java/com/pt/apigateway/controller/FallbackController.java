package com.pt.apigateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @RequestMapping(value = "/fallback-account")
    public String fallbackAccount() {
        return "account-service : error!";
    }

    @RequestMapping(value = "/fallback-blog")
    public String fallbackBlog() {
        return "blog-service : error!";
    }

    @RequestMapping(value = "/fallback-feign")
    public String fallbackFeign() {
        return "feign-service : error!";
    }
}
