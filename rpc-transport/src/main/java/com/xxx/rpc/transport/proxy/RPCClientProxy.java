package com.xxx.rpc.transport.proxy;

import com.xxx.rpc.common.config.RPCServiceConfig;
import com.xxx.rpc.common.enums.RPCErrorMessageEnum;
import com.xxx.rpc.common.enums.RPCResponseEnum;
import com.xxx.rpc.common.exceptions.RPCException;
import com.xxx.rpc.proto.RPCRequest;
import com.xxx.rpc.proto.RPCResponse;
import com.xxx.rpc.transport.RPCRequestTransport;
import com.xxx.rpc.transport.netty.client.NettyRPCClient;
import com.xxx.rpc.transport.socket.SocketRPCClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Dynamic proxy class.
 * When a dynamic proxy object calls a method, it actually calls the following invoke method.
 * It is precisely because of the dynamic proxy that the remote method called by the client is like calling the local method (the intermediate process is shielded)
 */
@Slf4j
public class RPCClientProxy implements InvocationHandler {

    private static final String INTERFACE_NAME = "interfaceName";

    /**
     * Used to send requests to the server.And there are two implementations: socket and netty
     */
    private final RPCRequestTransport rpcRequestTransport;
    private final RPCServiceConfig rpcServiceConfig;

    public RPCClientProxy(RPCRequestTransport rpcRequestTransport, RPCServiceConfig rpcServiceConfig) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = rpcServiceConfig;
    }


    public RPCClientProxy(RPCRequestTransport rpcRequestTransport) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = new RPCServiceConfig();
    }

    /**
     * get the proxy object
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * This method is actually called when you use a proxy object to call a method.
     * The proxy object is the object you get through the getProxy method.
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        log.info("invoked method: [{}]", method.getName());
        RPCRequest rpcRequest = RPCRequest.builder().methodName(method.getName())
                .params(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceConfig.getGroup())
                .version(rpcServiceConfig.getVersion())
                .build();
        RPCResponse<Object> rpcResponse = null;
        if (rpcRequestTransport instanceof NettyRPCClient) {
            CompletableFuture<RPCResponse<Object>> completableFuture =
                    (CompletableFuture<RPCResponse<Object>>) rpcRequestTransport.sendRpcRequest(rpcRequest);
            rpcResponse = completableFuture.get();
        }
        if (rpcRequestTransport instanceof SocketRPCClient) {
            rpcResponse = (RPCResponse<Object>) rpcRequestTransport.sendRpcRequest(rpcRequest);
        }
        this.check(rpcResponse, rpcRequest);
        return rpcResponse.getData();
    }

    private void check(RPCResponse<Object> rpcResponse, RPCRequest rpcRequest) {
        if (rpcResponse == null) {
            throw new RPCException(RPCErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RPCException(RPCErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RPCResponseEnum.SUCCESS.getCode())) {
            throw new RPCException(RPCErrorMessageEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
