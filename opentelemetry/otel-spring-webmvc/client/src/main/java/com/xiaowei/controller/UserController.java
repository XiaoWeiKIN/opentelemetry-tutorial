package com.xiaowei.controller;

import com.xiaowei.model.User;
import com.xiaowei.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: wangxw
 * @Date: 2021/12/24
 * @Description:
 */
@RestController
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUserService userService;

    @GetMapping("/user")
    public User getUser(@RequestParam("id") Long id) {
        logger.info("Get user by id {}", id);
        return userService.getUser(id);
    }

    @PostMapping("/user")
    public User saveUser(@RequestParam("name") String name) {
        logger.info("Save user with name {}", name);
        return userService.saveUser(name);
    }
}
