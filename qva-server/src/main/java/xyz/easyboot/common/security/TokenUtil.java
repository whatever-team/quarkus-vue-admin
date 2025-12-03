package xyz.easyboot.common.security;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.jwt.JWT;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.http.HttpServerRequest;
import xyz.easyboot.common.context.Qva;
import xyz.easyboot.common.util.StringUtil;
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
    
    public static String create(String userId, Date expireAt) {
        return JWT.create()
                .setExpiresAt(expireAt)
                .setPayload(S_USER_ID, userId)
                .setKey(KEY.getBytes())
                .sign();
    }
    
    public static String create(String userId) {
        Date expireAt = DateUtil.offsetDay(new Date(), 7);
        return create(userId, expireAt);
    }

    public static String getByRequest(HttpServerRequest request) {
        return getByRequest(request, true);
    }

    /**
     * 根据HTTP请求获取授权Token的工具方法。
     *
     * @param request       HTTP请求对象，从中提取Token信息。
     * @param removePrefix  是否移除Token前缀。如果Token是以"Bearer "为前缀的，则根据此参数决定是否去除该前缀。
     * @return 返回提取到的Token字符串。如果请求为null或未找到Token，则返回空字符串。
     */
    public static String getByRequest(HttpServerRequest request, boolean removePrefix) {
        if (request == null) {
            return "";
        }
        String token = request.getHeader(S_AUTHORIZATION);
        if (StringUtil.isEmpty(token)) {
            return "";
        }
        if (removePrefix) {
            token = StringUtil.subAfter(token, S_BEARER, true);
        }
        return token;
    }

    public static boolean verify(String token) {
        return JWT.of(token).setKey(KEY.getBytes()).verify();
    }
    
    public static String getSysUserId(String token) {
        String testTokenPrefix = Qva.getBean(AuthorizationConfig.class).testTokenPrefix();
        if (token.startsWith(testTokenPrefix)) {
            return StringUtil.subAfter(token, testTokenPrefix, true);
        }
        JWT jwt = JWT.of(token);
        return String.valueOf(jwt.getPayload(S_USER_ID));
    }
    
    public static SysUser getSysUser(String token) {
        return SysUser.findById(getSysUserId(token));
    }
    
}
