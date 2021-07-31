package xyz.easyboot.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.jwt.JWT;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.http.HttpServerRequest;
import xyz.easyboot.web.system.entity.SysUser;

import java.util.Date;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/4 9:14 下午
 */
@RegisterForReflection(targets = {SmUtil.class})
public class TokenUtil {
    
    private static final String KEY = "QUARKUS_VUE_ADMIN";
    private static final String S_USER_ID = "USER_ID";
    public static final String S_AUTHORIZATION = "Authorization";
    public static final String S_BEARER = "Bearer ";
    
    public static String create(Long userId, Date expireAt) {
        return JWT.create()
                .setExpiresAt(expireAt)
                .setPayload(S_USER_ID, userId)
                .setKey(KEY.getBytes())
                .sign();
    }
    
    public static String create(Long userId) {
        Date expireAt = DateUtil.offsetDay(new Date(), 7);
        return create(userId, expireAt);
    }
    
    public static String getByRequest(HttpServerRequest request) {
        if (request == null) {
            return "";
        }
        String token = request.getHeader(S_AUTHORIZATION);
        if (StringUtil.isEmpty(token)) {
            return "";
        }
        token = StringUtil.subAfter(token, S_BEARER, true);
        return token;
    }
    
    public static boolean verify(String token) {
        return JWT.of(token).setKey(KEY.getBytes()).verify();
    }
    
    public static Long getSysUserId(String token) {
        JWT jwt = JWT.of(token);
        return (Long) jwt.getPayload(S_USER_ID);
    }
    
    public static SysUser getSysUser(String token) {
        return SysUser.findById(getSysUserId(token));
    }
    
}
