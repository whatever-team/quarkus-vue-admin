package xyz.easyboot.common.context;

import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import xyz.easyboot.common.security.TokenUtil;

@Slf4j
public class SessionContext {

    public String getUserId() {
        HttpServerRequest request = Qva.getBean(HttpServerRequest.class);
        String token = TokenUtil.getByRequest(request);
        return TokenUtil.getSysUserId(token);
    }

}
