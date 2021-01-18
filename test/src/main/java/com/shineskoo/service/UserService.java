package com.shineskoo.service;

import com.shineskoo.dao.UserMapper;
import com.shineskoo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;


/**
 * Date: 2020/5/20
 * Author: ShinesKoo
 * Desc: 验证Token
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 验证该账号是否存在
     * @param account 账号
     * @return true/false
     */
    public boolean checkAccount(String account) {

        //查询数据库
        String pass = userMapper.selectByAccount(account);

        if (pass == null) {
            return false;
        }
        return true;
    }

    /**
     * 按照账号查询密码
     * @param account 账号
     * @return password
     */
    public String queryPass(String account) {
        String pass = userMapper.findPass(account);
        return pass;
    }

    public String queryIP(String account) {
        String ip = userMapper.findIP(account);
        return ip;
    }
}