package com.xxx.rpc.example.client;

import com.xxx.rpc.common.config.RPCServiceConfig;
import com.xxx.rpc.example.api.Hello;
import com.xxx.rpc.example.api.HelloService;
import com.xxx.rpc.transport.RPCRequestTransport;
import com.xxx.rpc.transport.netty.client.NettyRPCClient;
import com.xxx.rpc.transport.proxy.RPCClientProxy;

public class NettyClientMain {
    public static void main(String[] args) {
        RPCRequestTransport rpcRequestTransport = new NettyRPCClient();
        RPCServiceConfig rpcServiceConfig = RPCServiceConfig.builder()
                .group("test3").version("version3").build();
        RPCClientProxy rpcClientProxy = new RPCClientProxy(rpcRequestTransport, rpcServiceConfig);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("111", "222"));
        System.out.println(hello);
    }
}
