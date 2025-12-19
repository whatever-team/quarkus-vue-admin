package io.lighting.handler;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import io.lighting.common.base.dto.Result;
import io.lighting.common.base.message.impl.DefaultRespCodeMsg;
import io.lighting.common.util.StringUtil;
import jakarta.validation.ValidationException;

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
        return Response.status(resolveStatus(e)).entity(result).build();
    }

    private Result<?> buildExceptionResult(Throwable throwable) {
        String code;
        if (throwable instanceof UnauthorizedException) {
            code = DefaultRespCodeMsg.UNAUTHORIZED.getCode();
        } else if (throwable instanceof ForbiddenException) {
            code = DefaultRespCodeMsg.FORBIDDEN.getCode();
        } else if (throwable instanceof ValidationException || throwable instanceof IllegalArgumentException) {
            code = DefaultRespCodeMsg.FAIL.getCode();
        } else {
            code = DefaultRespCodeMsg.INTERNAL_SERVER_ERROR.getCode();
        }
        return new Result<>(code, throwable.toString());
    }

    private int resolveStatus(Throwable throwable) {
        if (throwable instanceof UnauthorizedException) {
            return Response.Status.UNAUTHORIZED.getStatusCode();
        }
        if (throwable instanceof ForbiddenException) {
            return Response.Status.FORBIDDEN.getStatusCode();
        }
        if (throwable instanceof ValidationException || throwable instanceof IllegalArgumentException) {
            return Response.Status.BAD_REQUEST.getStatusCode();
        }
        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }

}
