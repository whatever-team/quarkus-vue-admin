package xyz.easyboot.common.util;

import cn.hutool.crypto.SecureUtil;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/13 10:10 上午
 */
public class PasswordUtil {
    
    public static final String DEFAULT_PASSWORD = "000000";
    
    public static String encrypt(String password) {
        return SecureUtil.md5(password);
    }
    
}
