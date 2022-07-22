package com.xxx.rpc.transport.handler;

import com.xxx.rpc.common.exceptions.RPCException;
import com.xxx.rpc.common.factory.SingletonFactory;
import com.xxx.rpc.proto.RPCRequest;
import com.xxx.rpc.registry.provider.ServiceProvider;
import com.xxx.rpc.registry.provider.impl.ZkServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * RpcRequest processor
 *
 */
@Slf4j
public class RPCRequestHandler {
    private final ServiceProvider serviceProvider;

    public RPCRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    /**
     * Processing rpcRequest: call the corresponding method, and then return the method
     */
    public Object handle(RPCRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getRPCServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * get method execution results
     *
     * @param rpcRequest client request
     * @param service    service object
     * @return the result of the target method execution
     */
    private Object invokeTargetMethod(RPCRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParams());
            log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new RPCException(e.getMessage(), e);
        }
        return result;
    }
}
