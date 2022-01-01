package com.xiaowei.controller;

import com.xiaowei.model.User;
import com.xiaowei.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wangxw
 * @Date: 2021/12/24
 * @Description:
 */
@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/user")
    public User getUser(@RequestParam("id") Long id) {
        return userService.getUser(id);
    }
    @PostMapping("/user")
    public User saveUser(@RequestParam("name") String name) {
        return userService.saveUser(name);
    }
}
