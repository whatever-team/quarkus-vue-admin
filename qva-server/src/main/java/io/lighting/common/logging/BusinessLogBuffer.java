package io.lighting.common.logging;

import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import io.lighting.common.logging.entity.SysBusinessLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Asynchronous buffer for business logs, persisting in batches.
 */
@Slf4j
@ApplicationScoped
public class BusinessLogBuffer {

    private final BlockingQueue<SysBusinessLog> queue = new LinkedBlockingQueue<>(2000);
    private volatile boolean running = true;
    private Thread worker;
    private static final int BATCH_SIZE = 50;
    private static final long POLL_MILLIS = 200;

    @PostConstruct
    void start() {
        worker = new Thread(this::runLoop, "business-log-buffer");
        worker.setDaemon(true);
        worker.start();
    }

    public void enqueue(SysBusinessLog log) {
        if (!running || log == null) {
            return;
        }
        boolean offered = queue.offer(log);
        if (!offered) {
            this.log.warn("Business log queue is full, dropping log: {}", log.title);
        }
    }

    private void runLoop() {
        List<SysBusinessLog> batch = new ArrayList<>(BATCH_SIZE);
        while (running) {
            try {
                SysBusinessLog first = queue.poll(POLL_MILLIS, TimeUnit.MILLISECONDS);
                if (first != null) {
                    batch.add(first);
                    queue.drainTo(batch, BATCH_SIZE - batch.size());
                    flush(batch);
                    batch.clear();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("Business log buffer flush error", e);
            }
        }
        // flush remaining
        try {
            queue.drainTo(batch);
            flush(batch);
        } catch (Exception e) {
            log.error("Business log buffer final flush error", e);
        }
    }

    private void flush(List<SysBusinessLog> batch) {
        if (batch.isEmpty()) {
            return;
        }
        QuarkusTransaction.requiringNew().run(() -> SysBusinessLog.persist(batch));
    }

    @PreDestroy
    void stop() {
        running = false;
        if (worker != null) {
            worker.interrupt();
        }
    }
}
