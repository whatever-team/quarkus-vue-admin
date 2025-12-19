package io.lighting.common.logging;

import org.jboss.logging.MDC;

import java.util.UUID;

/**
 * Simple request correlation holder for logs and responses.
 */
public final class CorrelationContext {
    public static final String HEADER = "X-Request-Id";
    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    private CorrelationContext() {}

    public static String current() {
        return HOLDER.get();
    }

    public static String init(String incoming) {
        String id = incoming;
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
        HOLDER.set(id);
        MDC.put(HEADER, id);
        return id;
    }

    public static void clear() {
        HOLDER.remove();
        MDC.remove(HEADER);
    }
}
