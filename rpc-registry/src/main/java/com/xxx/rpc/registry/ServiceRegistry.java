package com.xxx.rpc.registry;

import com.xxx.rpc.common.annoation.SPI;

import java.net.InetSocketAddress;

@SPI
public interface ServiceRegistry {

    /**
     * 注册服务
     * @param rpcServiceName 服务名称
     * @param inetSocketAddress 服务地址
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
