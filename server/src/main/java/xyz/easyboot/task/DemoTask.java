package xyz.easyboot.task;

import io.quarkus.scheduler.Scheduled;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;

/**
 * 演示定时任务
 * @author wujiawei
 * @see
 * @since 2021/7/25 7:33 下午
 */
@Slf4j
@ApplicationScoped
public class DemoTask {
    
    @Scheduled(every = "60s", identity = "demo-task")
    void run() {
        log.info("This is demo task");
    }
    
}
