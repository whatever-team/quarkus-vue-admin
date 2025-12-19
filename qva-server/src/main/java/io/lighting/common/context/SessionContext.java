package io.lighting.common.context;

import lombok.extern.slf4j.Slf4j;
import io.quarkus.security.identity.SecurityIdentity;

@Slf4j
public class SessionContext {

    public String getUserId() {
        SecurityIdentity identity = Qva.getBean(SecurityIdentity.class);
        if (identity != null && identity.getPrincipal() != null) {
            return identity.getPrincipal().getName();
        }
        return null;
    }

}
