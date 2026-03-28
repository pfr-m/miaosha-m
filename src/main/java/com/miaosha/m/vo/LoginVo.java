package com.miaosha.m.vo;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
//VO（View Object），即视图对象，在 Java 开发（特别是 Web 开发）中主要用于封装前端页面（或客户端）发送给后端的数据。
@Data
public class LoginVo {
    @NotNull
    private String id; // 手机号统一标识

    @NotNull
    @Length(min=32)
    private String password;
}
