package com.miaosha.m.redis;

public class GoodsKey extends BasePrefix {

    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    // 默认永久有效，前缀为 "gs"
    public static GoodsKey getMiaoshaGoodsStock = new GoodsKey(0, "gs");
}
