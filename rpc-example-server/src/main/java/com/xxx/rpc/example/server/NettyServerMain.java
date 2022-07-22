package com.xxx.rpc.example.server;

import com.xxx.rpc.example.server.serviceImpl.HelloServiceImpl2;
import com.xxx.rpc.common.config.RPCServiceConfig;
import com.xxx.rpc.example.api.HelloService;
import com.xxx.rpc.transport.netty.server.NettyRPCServer;

public class NettyServerMain {
    public static void main(String[] args) {
        NettyRPCServer nettyRpcServer = new NettyRPCServer();
        // Register service manually
        HelloService helloService2 = new HelloServiceImpl2();
        RPCServiceConfig rpcServiceConfig = RPCServiceConfig.builder()
                .group("test3").version("version3").service(helloService2).build();
        nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
    }
}
