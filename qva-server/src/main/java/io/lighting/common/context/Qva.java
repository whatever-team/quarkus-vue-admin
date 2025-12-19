package io.lighting.common.context;

import jakarta.enterprise.inject.spi.CDI;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Qva {

    private Qva() {}

    public static SessionContext session() {
        return new SessionContext();
    }

    public static <T> T getBean(Class<T> clazz) {
        return CDI.current().select(clazz).get();
    }
}
