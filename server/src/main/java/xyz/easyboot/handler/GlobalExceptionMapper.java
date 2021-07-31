package xyz.easyboot.handler;

import lombok.extern.slf4j.Slf4j;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.base.message.impl.DefaultRespCodeMsg;
import xyz.easyboot.common.util.StringUtil;

import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

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
        Result<?> result = new Result<>(DefaultRespCodeMsg.FAIL.getCode(), e.toString());
        result.setMessage(StringUtil.emptyToDefault(e.getMessage(), e.toString()));
        return Response.status(Response.Status.OK).entity(result).build();
    }
    
}
