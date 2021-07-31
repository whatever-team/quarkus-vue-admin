package xyz.easyboot.common.base;

import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import xyz.easyboot.common.util.TokenUtil;
import xyz.easyboot.web.system.entity.SysUser;

import javax.ws.rs.core.Context;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/1 4:30 下午
 */
@Slf4j
public abstract class BaseResource {

    @Context
    protected HttpServerRequest request;
    
    public SysUser getUser() {
        String token = TokenUtil.getByRequest(request);
        return TokenUtil.getSysUser(token);
    }
    
    public Long getUserId() {
        String token = TokenUtil.getByRequest(request);
        return TokenUtil.getSysUserId(token);
    }
}
