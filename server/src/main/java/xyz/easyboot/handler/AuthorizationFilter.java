package xyz.easyboot.handler;

import io.vertx.core.http.HttpServerRequest;
import xyz.easyboot.common.util.StringUtil;
import xyz.easyboot.common.util.TokenUtil;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;

/**
 * 鉴权过滤器
 * @author wujiawei
 * @see
 * @since 2021/7/4 10:02 下午
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
            throw new RuntimeException("No Authorization");
        }
        boolean flag = TokenUtil.verify(token);
        if (!flag) {
            throw new RuntimeException("非法授权");
        }
    }
}
