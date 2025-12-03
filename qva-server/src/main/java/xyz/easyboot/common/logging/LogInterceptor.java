package xyz.easyboot.common.logging;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.quarkus.arc.Arc;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.Priority;
import jakarta.ws.rs.core.*;
import lombok.extern.slf4j.Slf4j;
import xyz.easyboot.common.base.dto.Result;
import xyz.easyboot.common.context.HttpContext;
import xyz.easyboot.common.logging.entity.SysBusinessLog;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.transaction.UserTransaction;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

@Slf4j
@Logged
@Interceptor
@Priority(2020)
public class LogInterceptor {
    
    @Inject
    UserTransaction userTransaction;
    @Context
    HttpServerRequest request;
    @Inject
    HttpContext httpContext;
    
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
        HttpHeaders headers = httpContext.getHttpHeaders();

        // 现在你可以访问 uriInfo 和 headers 来获取请求的路径和头信息
        String path = uriInfo.getPath();
        MultivaluedMap<String, String> headerMap = headers.getRequestHeaders();
        log.info("访问路径：{} 请求头：{}", path, headerMap);

        // 使用Arc容器获取RoutingContext
        RoutingContext routingContext = Arc.container().instance(RoutingContext.class).get();
        if (routingContext != null) {
            // 从RoutingContext获取header信息
            MultiMap headersMap = routingContext.request().headers();
            for (Map.Entry<String, String> header : headersMap.entries()) {
                System.out.println(header.getKey() + ": " + header.getValue());
            }

            // 示例：获取特定的header信息
            String specificHeader = headersMap.get("Your-Specific-Header-Name");

            // 如果需要，你可以在此处记录或使用这些header信息

        }

        SysBusinessLog businessLog = new SysBusinessLog();
        businessLog.clazz = className;
        businessLog.method = methodName;
        businessLog.parameter = parameterArray.toString();
        businessLog.title = logValue;
        if (ret instanceof Result<?> result) {
            businessLog.code = result.getCode();
            businessLog.message = result.getMessage();
            businessLog.response = JSONUtil.toJsonStr(result);
            businessLog.serial = result.getSerial();
            businessLog.exception = result.getException();
        }
        businessLog.duration = System.currentTimeMillis() - startMills;
        try {
//            userTransaction.begin();
            businessLog.save();
//            userTransaction.commit();
        } catch (Exception e) {
            log.error("BusinessLog save error", e);
//            userTransaction.rollback();
        }
    
        // ...log after
        return ret;
    }
    
}
