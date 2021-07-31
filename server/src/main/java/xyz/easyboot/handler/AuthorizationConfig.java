package xyz.easyboot.handler;

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
    
}
