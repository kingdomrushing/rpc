package com.xxx.rpc.example.server;

import com.xxx.rpc.common.config.RPCServiceConfig;
import com.xxx.rpc.example.api.HelloService;
import com.xxx.rpc.example.server.serviceImpl.HelloServiceImpl1;
import com.xxx.rpc.transport.socket.SocketRPCServer;

public class SocketServerMain {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl1();
        SocketRPCServer socketRpcServer = new SocketRPCServer();
        RPCServiceConfig rpcServiceConfig = new RPCServiceConfig();
        rpcServiceConfig.setService(helloService);
        socketRpcServer.registerService(rpcServiceConfig);
        socketRpcServer.start();
    }
}
