package com.miaosha.m.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
// ... 其他导入
@Component
public class AccessInterceptor implements HandlerInterceptor {
    // 这里实现具体的限流逻辑，目前可以先写个空实现让代码不报错
}
