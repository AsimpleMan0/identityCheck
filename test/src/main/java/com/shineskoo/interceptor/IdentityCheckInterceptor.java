package com.shineskoo.interceptor;

/*
 *   @Author: Cosmos
 *   @Date: 2021/1/13 4:24 下午
 */

import com.shineskoo.service.UserService;
import com.shineskoo.toolkit.IP.IpUtil;
import com.shineskoo.toolkit.StringUtil;
import com.shineskoo.toolkit.sha256.SHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 身份验证
 */
@Component("identityCheckInterceptor")
public class IdentityCheckInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;
    /**
     * 进入controller之前拦截请求，过滤条件如下：
     *  1. 是否存在该账号
     *  2. 是否超时（2min）
     *  3. 验证签名
     *  4. ip校验
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // 获取request中信息
            RequestWrapper requestWrapper = new RequestWrapper(request);
            // 请求体中的信息
            String body = requestWrapper.getBody();
            // 将请求体中信息存入map
            Map<String, String> info = StringUtil.stringToMap(body);

            // 1.检查是否存在该账号
            String account = info.get("account");
            System.out.println("account:" + account);
            boolean res = userService.checkAccount(account);
            if (!res) {
                System.out.println("账号不存在");
                sendErrorMsg(response, "this account is not exit");
                return false;
            }

            // 2.请求是否超时
            String time = info.get("timeStamp");
            long curTime = System.currentTimeMillis() / 1000;
            long timeStamp = Long.valueOf(time);
            // 判断接收到的时间是否超过两分钟
            if (curTime - timeStamp >= (60 * 2)) {
                System.out.println("请求超时");
                sendErrorMsg(response, "timeout");
                return false;
            }

            // 3.验证签名
            // 从数据库找到该账号的密码
            String password = userService.queryPass(account);

            /*
                加密的流程：sign = account + password + timeStamp + 随机字符串 对sign进行sha256加密
            */

            StringBuilder sb = new StringBuilder();
            sb.append(account).append(password).append(time);
            // 使用sha256算法获得加密后的内容
            String encrypt = SHA.encrypt(sb.toString());
            // 获取post请求传过来的签名
            String sign = info.get("sign");
            // 对比校验
            if (!encrypt.equals(sign)) {
                System.out.println("签名不正确");
                sendErrorMsg(response, "signature is wrong");
                return false;
            }

            // 4.ip校验
            // 通过post请求获取发送方IP
            String realIP = IpUtil.getRealIP(request);
            // 通过数据库查询该账户对应的IP
            String ip = userService.queryIP(account);
            // 比对IP是否相同
            if (!ip.equals(realIP)) {
                System.out.println("ip检验不通过");
                sendErrorMsg(response, "illegal IP address");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 根据desc，返回对应的错误消息
     * @param response response
     * @param desc 错误信息描述
     * @return response
     */
    private void sendErrorMsg(HttpServletResponse response, String desc) {
        // 重置response
        response.reset();
        // 设置编码格式
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(desc);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
    }
}
