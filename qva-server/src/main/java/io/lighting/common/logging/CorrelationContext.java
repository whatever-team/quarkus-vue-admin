package io.lighting.common.logging;

import org.jboss.logging.MDC;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Simple request correlation holder for logs and responses.
 */
public final class CorrelationContext {
    public static final String HEADER = "X-Request-Id";
    private static final InheritableThreadLocal<String> HOLDER = new InheritableThreadLocal<>();

    private CorrelationContext() {}

    public static String current() {
        return HOLDER.get();
    }

    public static String init(String incoming) {
        String id = incoming;
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
        return set(id);
    }

    public static String ensure() {
        String id = HOLDER.get();
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
        return set(id);
    }

    public static void clear() {
        HOLDER.remove();
        MDC.remove(HEADER);
    }

    public static Runnable wrap(Runnable task) {
        String id = ensure();
        return () -> runWith(id, task);
    }

    public static <T> Supplier<T> wrap(Supplier<T> task) {
        String id = ensure();
        return () -> callWith(id, task::get);
    }

    public static <T> Callable<T> wrap(Callable<T> task) {
        String id = ensure();
        return () -> callWith(id, task);
    }

    private static void runWith(String id, Runnable task) {
        String previous = HOLDER.get();
        set(id);
        try {
            task.run();
        } finally {
            restore(previous);
        }
    }

    private static <T> T callWith(String id, Callable<T> task) {
        String previous = HOLDER.get();
        set(id);
        try {
            return task.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            restore(previous);
        }
    }

    private static String set(String id) {
        HOLDER.set(id);
        MDC.put(HEADER, id);
        return id;
    }

    private static void restore(String previous) {
        if (previous == null || previous.isBlank()) {
            clear();
        } else {
            set(previous);
        }
    }
}
