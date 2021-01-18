package com.shineskoo.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: 2021/1/15 11:06 上午
 * Author: Cosmos
 * Desc: 测试签名Controller(表现层)
 */
@RestController
@Component
@RequestMapping("/test")
public class TestSignController {

    @RequestMapping(value = "/show", method = RequestMethod.POST)
    public String exam(HttpServletRequest res, @RequestBody String user) {
        System.out.println(user.toString());
        return "hello";
    }

}
