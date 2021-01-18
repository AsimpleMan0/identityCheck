package com.shineskoo.pojo;

/*
 *   @Author: Cosmos
 *   @Date: 2021/1/15 2:15 下午
 */

import lombok.Data;

@Data
public class User {
    // 账号
    private String account;
    // 密码
    private String password;
    // IP地址
    private String ip;
}
