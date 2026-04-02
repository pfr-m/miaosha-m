package com.miaosha.m;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//开启Mapper接口扫描
@MapperScan("com.miaosha.m.mapper")
public class MiaoshaMApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaoshaMApplication.class, args);
    }

}
