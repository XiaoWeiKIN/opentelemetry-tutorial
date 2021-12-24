package com.xiaowei.controller;

import com.xiaowei.model.User;
import com.xiaowei.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wangxw
 * @Date: 2021/12/24
 * @Description:
 */
@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    public User getUser(@RequestParam("name") String name) {
        return userService.getUser(name);
    }
}
