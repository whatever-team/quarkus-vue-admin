package xyz.easyboot.common.base.message.impl;

import xyz.easyboot.common.base.message.RespCodeMsg;

/**
 * @author wujiawei
 * @see
 * @since 2021/6/30 8:54 下午
 */
public enum DefaultRespCodeMsg implements RespCodeMsg {
    SUCCESS("操作成功"),
    FAIL("操作失败"),
    TOO_MANY_REQUEST("请求过于频繁"),
    REQ_BLOCKED("请求被阻拦。可能原因：1. 系统繁忙；2. 没有访问权限。"),
    SERVICE_UNAVAILABLE("服务暂时不可用，请稍后再试"),
    BAD_REQUEST("错误的请求"),
    UNAUTHORIZED("未认证，请先登录"),
    NOT_FOUND("访问的资源不存在"),
    FORBIDDEN("不允许访问"),
    INTERNAL_SERVER_ERROR("服务器异常"),
    ;
    
    private String message;
    
    public String getMessage() {
        return this.message;
    }
    
    private DefaultRespCodeMsg(String message) {
        this.message = message;
    }
    
    public String getCode() {
        return this.name();
    }
    
    public String getMsg() {
        return this.getMessage();
    }
}
