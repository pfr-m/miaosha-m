package com.miaosha.m.redis;

public class MiaoShaUserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;

    private MiaoShaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    // 1. 分布式 Session Token (tk)
    public static MiaoShaUserKey token = new MiaoShaUserKey(TOKEN_EXPIRE, "tk");

    // 2. 新增：根据 ID（手机号）缓存用户信息 (id)
    // 建议设置为永久有效（0），或者较长的过期时间
    public static MiaoShaUserKey getById = new MiaoShaUserKey(0, "id");

    // 3. 保留（可选）：如果确实需要通过昵称查询再保留
    public static MiaoShaUserKey getByNickName = new MiaoShaUserKey(0, "nickname");
}
