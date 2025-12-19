package io.lighting.common.security;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

/**
 * authorization.permit-patterns
 */
@ConfigMapping(prefix = "authorization", namingStrategy = ConfigMapping.NamingStrategy.KEBAB_CASE)
public interface AuthorizationConfig {
    
    /**
     * 无需鉴权的路径Pattern
     * @return
     */
    @WithDefault("test")
    String permitPatterns();

    /**
     * 是否开启登录验证码
     * @return
     */
    @WithDefault("false")
    Boolean captchaEnable();

    /**
     * 是否允许测试token
     * @return
     */
    @WithDefault("false")
    Boolean testTokenAllowed();

    /**
     * 测试token前缀
     * @return
     */
    @WithDefault("qva-test-")
    String testTokenPrefix();
    
}
