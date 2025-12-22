package io.lighting.common.security;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * authorization.* configuration wrapper.
 */
@ApplicationScoped
public class AuthorizationConfig {

    @ConfigProperty(name = "authorization.permit-patterns", defaultValue = "test")
    String permitPatterns;

    @ConfigProperty(name = "authorization.captcha-enable", defaultValue = "false")
    boolean captchaEnable;

    @ConfigProperty(name = "authorization.test-token-allowed", defaultValue = "false")
    boolean testTokenAllowed;

    @ConfigProperty(name = "authorization.test-token-prefix", defaultValue = "qva-test-")
    String testTokenPrefix;

    public String permitPatterns() {
        return permitPatterns;
    }

    public boolean captchaEnable() {
        return captchaEnable;
    }

    public boolean testTokenAllowed() {
        return testTokenAllowed;
    }

    public String testTokenPrefix() {
        return testTokenPrefix;
    }
}
