package com.xiaowei.service;

import com.xiaowei.dao.UserRepository;
import com.xiaowei.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: wangxw
 * @Date: 2021/12/24
 * @Description:
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseGet(()->User.builder()
                        .id(id)
                        .name("N/A")
                        .age(27).build());
    }

    @Override
    public User saveUser(String name) {
        return userRepository.save(User.builder()
                .name(name)
                .age(27).build());
    }
}
