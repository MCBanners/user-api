package com.mcbanners.userapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("restricted")
public class RestrictedController {
    @RequestMapping("test")
    public String test() {
        return "Restricted";
    }
}
