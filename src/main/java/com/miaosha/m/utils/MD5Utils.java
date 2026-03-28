package com.miaosha.m.utils;

import org.apache.commons.codec.digest.DigestUtils;
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;

public class MD5Utils {

    // 固定盐值：用于“用户端 -> 服务端”的第一层加密。
    // 注意：这个盐值必须和前端页面上的 JS 保持一致！
    private static final String SALT = "1a2b3c4d";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    /**
     * 第一次 MD5：用户输入的明文密码 -> 表单传输的密码
     * 目的：防止密码在网络上明文传输
     */
    public static String inputPassToFormPass(String inputPass) {
        String str = "" + SALT.charAt(0) + SALT.charAt(2) + inputPass + SALT.charAt(5) + SALT.charAt(4);
        return md5(str);
    }

    /**
     * 第二次 MD5：表单密码 -> 数据库存储密码
     * @param formPass 表单传来的已经经过一次 MD5 的密码
     * @param saltDb 数据库中为每个用户随机生成的盐值
     */
    public static String formPassToDBPass(String formPass, String saltDb) {
        String str = "" + saltDb.charAt(0) + saltDb.charAt(2) + formPass + saltDb.charAt(5) + saltDb.charAt(4);
        return md5(str);
    }

    /**
     * 合并步骤：直接从明文转为数据库密文（注册时用）
     */
    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    /**
     * 生成随机盐值（注册用户时用）
     */
    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[15];
        random.nextBytes(bytes);
        return Base64.encodeBase64String(bytes);
    }
}
