package xyz.easyboot.common.logging;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.logging.entity.SysBusinessLog;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.UserTransaction;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author wujiawei
 * @see
 * @since 2021/6/27 10:02 下午
 */
@Slf4j
@BusinessLog
@Priority(10)
@Interceptor
@ApplicationScoped
public class LogInterceptor {
    
    @Inject
    UserTransaction userTransaction;
    
    @AroundInvoke
    Object logInvocation(InvocationContext context) throws Exception {
        long startMills = System.currentTimeMillis();
        Method method = context.getMethod();
        String logValue = method.getAnnotation(BusinessLog.class).value();
        String logGroup = method.getAnnotation(BusinessLog.class).group();
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
        
        SysBusinessLog businessLog = new SysBusinessLog();
        businessLog.clazz = className;
        businessLog.method = methodName;
        businessLog.parameter = parameterArray.toString();
        businessLog.title = logValue;
        if (ret instanceof Result) {
            Result<?> result = (Result<?>) ret;
            businessLog.code = result.getCode();
            businessLog.message = result.getMessage();
            businessLog.response = JSONUtil.toJsonStr(result);
            businessLog.serial = result.getSerial();
            businessLog.exception = result.getException();
        }
        businessLog.duration = System.currentTimeMillis() - startMills;
        try {
            userTransaction.begin();
            businessLog.save();
            userTransaction.commit();
        } catch (Exception e) {
            log.error("BusinessLog save error", e);
            userTransaction.rollback();
        }
    
        // ...log after
        return ret;
    }
    
}
