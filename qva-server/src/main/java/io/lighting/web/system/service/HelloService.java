package io.lighting.web.system.service;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * @author wujiawei
 * @see
 * @since 2021/6/22 9:31 上午
 */
@ApplicationScoped
public class HelloService {

    public String hello(String name){
        return "hello " + name;
    }
    
}
