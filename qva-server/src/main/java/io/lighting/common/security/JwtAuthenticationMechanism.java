package io.lighting.common.security;

import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.lighting.common.logging.CorrelationContext;
import io.lighting.common.util.StringUtil;
import io.lighting.web.system.entity.SysUser;
import io.lighting.web.system.entity.SysUserRole;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 基于 Bearer Token 的认证机制，接入 Quarkus 安全管线。
 */
@ApplicationScoped
public class JwtAuthenticationMechanism implements HttpAuthenticationMechanism {

    private static final ChallengeData BEARER_CHALLENGE = new ChallengeData(401, "WWW-Authenticate", "Bearer");

    @Inject
    AuthorizationConfig authorizationConfig;

    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context, io.quarkus.security.identity.IdentityProviderManager identityProviderManager) {
        final String path = context.normalisedPath();
        if (isPermitPath(path)) {
            return Uni.createFrom().nullItem();
        }

        String token = TokenUtil.getByRequest(context.request(), true);
        if (StringUtil.isEmpty(token)) {
            return Uni.createFrom().failure(new AuthenticationFailedException("Missing Authorization header"));
        }

        return Uni.createFrom().item(CorrelationContext.wrap(() -> buildIdentity(token)))
                .runSubscriptionOn(Infrastructure.getDefaultExecutor());
    }

    private SecurityIdentity buildIdentity(String token) {
        if (!authorizationConfig.testTokenAllowed() && !TokenUtil.verify(token)) {
            throw new AuthenticationFailedException("Invalid token");
        }
        String userId = TokenUtil.getSysUserId(token);
        SysUser user = SysUser.findById(userId);
        if (user == null) {
            throw new AuthenticationFailedException("User not found");
        }
        List<SysUserRole> roles = SysUserRole.list("userId", user.id);
        Set<String> roleNames = roles.stream()
                .map(r -> r.role)
                .map(role -> role != null ? role.roleName : null)
                .filter(StringUtil::isNotEmpty)
                .collect(Collectors.toSet());

        QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder();
        builder.setPrincipal(new Principal() {
            @Override
            public String getName() {
                return userId;
            }
        });
        roleNames.forEach(builder::addRole);
        builder.addAttribute("user", user);
        builder.addAttribute("token", token);
        return builder.build();
    }

    private boolean isPermitPath(String path) {
        List<String> patterns = StringUtil.split(authorizationConfig.permitPatterns(), ',');
        return patterns.stream().anyMatch(path::startsWith);
    }

    @Override
    public Uni<ChallengeData> getChallenge(RoutingContext context) {
        return Uni.createFrom().item(BEARER_CHALLENGE);
    }
}
