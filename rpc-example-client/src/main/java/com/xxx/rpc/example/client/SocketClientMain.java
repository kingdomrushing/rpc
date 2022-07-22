package com.xxx.rpc.example.client;


import com.xxx.rpc.common.config.RPCServiceConfig;
import com.xxx.rpc.example.api.Hello;
import com.xxx.rpc.example.api.HelloService;
import com.xxx.rpc.transport.RPCRequestTransport;
import com.xxx.rpc.transport.proxy.RPCClientProxy;
import com.xxx.rpc.transport.socket.SocketRPCClient;

public class SocketClientMain {
    public static void main(String[] args) {
        RPCRequestTransport rpcRequestTransport = new SocketRPCClient();
        RPCServiceConfig rpcServiceConfig = new RPCServiceConfig();
        RPCClientProxy rpcClientProxy = new RPCClientProxy(rpcRequestTransport, rpcServiceConfig);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("111", "222"));
        System.out.println(hello);
    }
}
