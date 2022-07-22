package com.xxx.rpc.example.server.serviceImpl;

import com.xxx.rpc.example.api.Hello;
import com.xxx.rpc.example.api.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServiceImpl1 implements HelloService {

    static {
        System.out.println("HelloServiceImpl1被创建");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl1收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("HelloServiceImpl1返回: {}.", result);
        return result;
    }
}
