package xyz.easyboot.handler;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

/**
 * oss.store-path
 */
@ConfigMapping(prefix = "oss", namingStrategy = ConfigMapping.NamingStrategy.KEBAB_CASE)
public interface OssConfig {
    
    /**
     * 无需鉴权的路径Pattern
     * @return
     */
    @WithDefault("./oss/")
    String storePath();
    
    /**
     * 临时文件的路径
     * @return
     */
    @WithDefault("./oss/temp")
    String tempStorePath();
    
}
