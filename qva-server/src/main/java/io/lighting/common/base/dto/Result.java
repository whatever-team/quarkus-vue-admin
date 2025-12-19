package io.lighting.common.base.dto;

import cn.hutool.core.util.IdUtil;
import lombok.Data;
import io.lighting.common.base.message.RespCodeMsg;
import io.lighting.common.base.message.impl.DefaultRespCodeMsg;
import io.lighting.common.logging.CorrelationContext;

/**
 * 通用的接口返回值
 * @author wujiawei
 * @see
 * @since 2021/6/29 10:36 下午
 */
@Data
public class Result<T> {
    
    protected String code;
    
    protected String message;
    
    /**
     * 异常信息
     */
    protected String exception;
    
    /**
     * 流水请求号
     */
    protected String serial;
    
    protected ResultData<T> result;
    
    public static Result<Object> defaultResp() {
        return new Result<Object>(DefaultRespCodeMsg.SUCCESS);
    }
    
    private Result(){}
    
    public Result(String code, String exception) {
        this.code = code;
        this.exception = exception;
        this.serial = defaultSerial();
    }
    
    public Result(RespCodeMsg respCodeMsg) {
        if (respCodeMsg == null) {
            respCodeMsg = DefaultRespCodeMsg.SUCCESS;
        }
    
        this.code = respCodeMsg.getCode();
        this.message = respCodeMsg.getMsg();
        this.serial = defaultSerial();
    }
    
    public Result(T data) {
        this(data, null);
    }
    
    public Result(T data, PageInfo<?> pageInfo) {
        this(data, pageInfo, DefaultRespCodeMsg.SUCCESS.getMsg());
    }
    
    public Result(T data, PageInfo<?> pageInfo, String message) {
        this.code = DefaultRespCodeMsg.SUCCESS.getCode();
        this.message = message;
        this.result = new ResultData<>();
        this.result.setData(data);
        this.result.setPage(pageInfo);
        this.serial = defaultSerial();
    }

    private String defaultSerial() {
        String corr = CorrelationContext.current();
        return corr != null ? corr : "gateway_" + IdUtil.objectId();
    }
    
}
