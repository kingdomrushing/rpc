package com.xxx.rpc.registry.provider;

import com.xxx.rpc.common.config.RPCServiceConfig;

/**
 * store and provide service object.
 *
 */
public interface ServiceProvider {

    /**
     * 将service存到本地缓存中
     * @param rpcServiceConfig rpc service related attributes
     */
    void addService(RPCServiceConfig rpcServiceConfig);

    /**
     * 根据服务名获取某项服务
     * @param rpcServiceName rpc service name
     * @return service object
     */
    Object getService(String rpcServiceName);

    /**
     * 将service存到本地缓存中，并注册到注册中心
     * @param rpcServiceConfig rpc service related attributes
     */
    void publishService(RPCServiceConfig rpcServiceConfig);

}
