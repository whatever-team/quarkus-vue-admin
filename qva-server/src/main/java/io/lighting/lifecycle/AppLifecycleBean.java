package io.lighting.lifecycle;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import io.lighting.common.util.FileUtil;
import io.lighting.handler.OssConfig;

/**
 * 监听启动及关闭事件
 * @author wujiawei
 * @see
 * @since 2021/7/18 4:42 下午
 */
@ApplicationScoped
public class AppLifecycleBean {
    
    @Inject
    Logger logger;
    @Inject
    OssConfig ossConfig;
    
    void onStart(@Observes StartupEvent ev) {
        FileUtil.mkdir(ossConfig.storePath());
        FileUtil.mkdir(ossConfig.tempStorePath());
        logger.info("The application is starting...");
    }
    
    void onStop(@Observes ShutdownEvent ev) {
        logger.info("The application is stopping...");
    }
    
}
