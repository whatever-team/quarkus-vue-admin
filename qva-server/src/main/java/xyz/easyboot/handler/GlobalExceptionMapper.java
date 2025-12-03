package xyz.easyboot.handler;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.base.message.impl.DefaultRespCodeMsg;
import xyz.easyboot.common.util.StringUtil;

/**
 * @author wujiawei
 * @see
 * @since 2021/7/2 2:38 下午
 */
@Slf4j
@Provider
@Priority(12)
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    
    @Override
    public Response toResponse(Throwable e) {
        log.error(e.getMessage(), e);
        Result<?> result = buildExceptionResult(e);
        result.setMessage(StringUtil.emptyToDefault(e.getMessage(), e.toString()));
        return Response.status(Response.Status.OK).entity(result).build();
    }

    private Result<?> buildExceptionResult(Throwable throwable) {
        String code;
        if (throwable instanceof UnauthorizedException) {
            code = DefaultRespCodeMsg.UNAUTHORIZED.getCode();
        } else if (throwable instanceof ForbiddenException) {
            code = DefaultRespCodeMsg.FORBIDDEN.getCode();
        } else {
            code = DefaultRespCodeMsg.INTERNAL_SERVER_ERROR.getCode();
        }
        return new Result<>(code, throwable.toString());
    }

}
