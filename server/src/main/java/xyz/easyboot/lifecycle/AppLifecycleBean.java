package xyz.easyboot.lifecycle;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;
import xyz.easyboot.common.util.FileUtil;
import xyz.easyboot.handler.OssConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

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
