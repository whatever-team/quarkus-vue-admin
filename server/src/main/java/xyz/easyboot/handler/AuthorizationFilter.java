package xyz.easyboot.handler;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import xyz.easyboot.common.util.StringUtil;
import xyz.easyboot.common.util.TokenUtil;

import java.io.IOException;
import java.util.List;

/**
 * 鉴权过滤器
 * @author wujiawei
 * @see
 */
@Provider
@PreMatching
public class AuthorizationFilter implements ContainerRequestFilter {
    
    @Context
    HttpServerRequest request;
    @Inject
    AuthorizationConfig config;
    
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        List<String> patterns = StringUtil.split(config.permitPatterns(), ',');
        String path = containerRequestContext.getUriInfo().getPath();
        boolean permit = patterns.stream().anyMatch(path::contains);
        if (permit) {
            return;
        }
        String token = TokenUtil.getByRequest(request);
        if (StringUtil.isEmpty(token)) {
            throw new UnauthorizedException("No Authorization");
        }
        boolean flag = TokenUtil.verify(token);
        if (!flag) {
            throw new ForbiddenException("非法授权");
        }
    }
}
