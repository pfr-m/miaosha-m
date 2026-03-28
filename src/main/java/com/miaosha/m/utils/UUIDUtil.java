package com.miaosha.m.utils;

import java.util.UUID;

public class UUIDUtil {
    public static String uuid() {
        // 去掉原生 UUID 中的横杠 "-"
        return UUID.randomUUID().toString().replace("-", "");
    }
}
