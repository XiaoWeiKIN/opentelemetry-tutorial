package com.xiaowei.service;

import com.xiaowei.model.User;
import org.springframework.stereotype.Service;

/**
 * @Author: wangxw
 * @Date: 2021/12/24
 * @Description:
 */
@Service
public class UserServiceImpl implements IUserService {
    @Override
    public User getUser(String name) {
        return User.builder()
                .name(name)
                .id(1)
                .age(27).build();
    }
}
