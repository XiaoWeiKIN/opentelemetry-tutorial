package com.xiaowei.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: wangxw
 * @Date: 2021/12/24
 * @Description:
 */
@Data
@Builder
public class User {
    private String name;
    private Integer id;
    private Integer age;
}
