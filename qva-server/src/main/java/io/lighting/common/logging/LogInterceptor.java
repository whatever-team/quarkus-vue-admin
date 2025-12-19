package io.lighting.common.logging;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Priority;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;
import io.lighting.common.base.dto.Result;
import io.lighting.common.context.HttpContext;
import io.lighting.common.logging.entity.SysBusinessLog;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Slf4j
@Logged
@Interceptor
@Priority(2020)
public class LogInterceptor {
    
    @Inject
    HttpContext httpContext;
    @Inject
    BusinessLogBuffer logBuffer;
    
    @AroundInvoke
    public Object logMethodCall(InvocationContext context) throws Exception {
        long startMills = System.currentTimeMillis();
        Method method = context.getMethod();
        String logValue = method.getAnnotation(Logged.class).value();
        String logGroup = method.getAnnotation(Logged.class).group();
        Parameter[] parameters = method.getParameters();
        String methodName = context.getMethod().getName();
        String className = context.getMethod().getDeclaringClass().getName();
        Object[] objects = context.getParameters();
    
        JSONArray parameterArray = new JSONArray();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String name = parameter.getName();
            Object ob = objects[i];
    
            if (ob != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.set(name, ob);
                parameterArray.add(jsonObject);
            }
        }
        
        // process...
        Object ret = context.proceed();
        // process...

        UriInfo uriInfo = httpContext.getUriInfo();
        String path = uriInfo != null ? uriInfo.getPath() : "unknown";

        SysBusinessLog businessLog = new SysBusinessLog();
        businessLog.serial = CorrelationContext.current();
        businessLog.clazz = className;
        businessLog.method = methodName;
        businessLog.parameter = parameterArray.toString();
        businessLog.title = logValue;
        businessLog.url = path;
        if (ret instanceof Result<?> result) {
            businessLog.code = result.getCode();
            businessLog.message = result.getMessage();
            businessLog.response = JSONUtil.toJsonStr(result);
            businessLog.serial = result.getSerial();
            businessLog.exception = result.getException();
        }
        businessLog.duration = System.currentTimeMillis() - startMills;
        logBuffer.enqueue(businessLog);
    
        // ...log after
        return ret;
    }
    
}
