package com.miaosha.m.redis;

public interface KeyPrefix {

    /**
     * 有效期（秒）
     * @return 0 代表永不过期
     */
    int expireSeconds();

    /**
     * 获取前缀（如 "MiaoshaUserKey:id"）
     * @return 拼接后的前缀字符串
     */
    String getPrefix();
}
