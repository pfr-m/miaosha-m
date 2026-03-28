package com.miaosha.m.controller;

import com.miaosha.m.result.Result;
import com.miaosha.m.service.MiaoshaUserService;
import com.miaosha.m.vo.LoginVo;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Valid
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MiaoshaUserService userService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, LoginVo loginVo) {
        // 1. 现在 Lombok 好了，loginVo.toString() 可以正常打印出所有字段信息
        log.info("登录请求参数：{}", loginVo.toString());

        // 2. 业务逻辑调用
        // 只要你在 Service 内部已经把方法改成了 getById(loginVo.getNickname())，
        // 这里的调用就完全没问题。
        userService.login(response, loginVo);
        return Result.success(true);
    }
}
