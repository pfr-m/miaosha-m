package com.miaosha.m.service;

import com.miaosha.m.entity.MiaoshaUser;
import com.miaosha.m.exception.GlobalException;
import com.miaosha.m.mapper.MiaoshaUserMapper;
import com.miaosha.m.redis.MiaoShaUserKey;
import com.miaosha.m.redis.RedisService;
import com.miaosha.m.result.CodeMsg;
import com.miaosha.m.utils.MD5Utils;
import com.miaosha.m.utils.UUIDUtil;
import com.miaosha.m.vo.LoginVo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiaoshaUserService {

    public static final String COOKIE_NAME_TOKEN = "token";
    private static final Logger logger = LoggerFactory.getLogger(MiaoshaUserService.class);

    @Autowired
    private MiaoshaUserMapper miaoshaUserMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 根据 ID（手机号）获取用户对象
     * 优化：统一使用 MiaoShaUserKey.getById 对应 Redis 前缀 "id"
     */
    public MiaoshaUser getById(String id) {
        // 1. 先从 Redis 缓存中读取
        // 修改点：使用新定义的 getById 变量
        MiaoshaUser user = redisService.get(MiaoShaUserKey.getById, id, MiaoshaUser.class);
        if (user != null) {
            return user;
        }

        // 2. 缓存未命中，从数据库读取
        user = miaoshaUserMapper.getById(id);

        // 3. 将结果写入缓存，方便下次查询
        if (user != null) {
            redisService.set(MiaoShaUserKey.getById, id, user);
        }
        return user;
    }

    /**
     * 更新密码逻辑
     * 遵循缓存一致性策略：先更新数据库，再删除/更新缓存
     */
    public boolean updatePassword(String token, String id, String formPass) {
        // 1. 取出对象，确保用户存在并获取 salt
        MiaoshaUser user = getById(id);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        // 2. 更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(Long.parseLong(id));
        toBeUpdate.setPassword(MD5Utils.formPassToDBPass(formPass, user.getSalt()));
        miaoshaUserMapper.update(toBeUpdate);

        // 3. 处理缓存一致性（关键点）
        // 修改点：删除对应 "id" 前缀的缓存数据，保证下次 getById 拿到的是最新的
        redisService.delete(MiaoShaUserKey.getById, id);

        // 更新分布式 Session (Token) 中的用户信息，防止用户需要重新登录
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoShaUserKey.token, token, user);

        return true;
    }

    /**
     * 登录逻辑
     */
    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }

        String mobileId = loginVo.getId();
        String password = loginVo.getPassword();

        // 1. 验证手机号
        MiaoshaUser user = getById(mobileId);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        // 2. 验证密码（二次 MD5 校验）
        String dbPass = user.getPassword();
        String saltDb = user.getSalt();
        String calcPass = MD5Utils.formPassToDBPass(password, saltDb);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        // 3. 生成分布式 Session Token 并存入 Cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return true;
    }

    /**
     * 从 Redis 中根据 Token 获取用户信息（用于 ArgumentResolver）
     */
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoShaUserKey.token, token, MiaoshaUser.class);
        if (user != null) {
            // 延长有效期（只要用户在操作，Session 就不失效）
            addCookie(response, token, user);
        }
        return user;
    }

    /**
     * 生成分布式 Session 的私有辅助方法
     */
    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        // 1. 将 Token 和用户信息关联，存入 Redis (设置过期时间，比如 2天)
        redisService.set(MiaoShaUserKey.token, token, user);

        // 2. 将 Token 写入 Cookie 返回给客户端
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoShaUserKey.token.expireSeconds());
        cookie.setPath("/"); // 设置为根路径，确保全站可用
        response.addCookie(cookie);
    }
}
