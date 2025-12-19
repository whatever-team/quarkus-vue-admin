package io.lighting.common.base;

import lombok.extern.slf4j.Slf4j;
import io.quarkus.security.identity.SecurityIdentity;
import io.lighting.common.security.TokenUtil;
import io.lighting.web.system.entity.SysUser;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/1 4:30 下午
 */
@Slf4j
public abstract class BaseResource {

    @jakarta.inject.Inject
    protected SecurityIdentity securityIdentity;
    
    public SysUser getUser() {
        SysUser user = securityIdentity.getAttribute("user");
        if (user != null) {
            return user;
        }
        // fallback
        String token = securityIdentity.getAttribute("token");
        if (token != null) {
            return TokenUtil.getSysUser(token);
        }
        return null;
    }
    
    public String getUserId() {
        if (securityIdentity != null && securityIdentity.getPrincipal() != null) {
            return securityIdentity.getPrincipal().getName();
        }
        String token = securityIdentity.getAttribute("token");
        return TokenUtil.getSysUserId(token);
    }
}
