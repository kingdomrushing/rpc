package com.xxx.rpc.registry;

import com.xxx.rpc.common.annoation.SPI;
import com.xxx.rpc.proto.RPCRequest;

import java.net.InetSocketAddress;

/**
 * service discovery
 */
@SPI
public interface ServiceDiscovery {
    /**
     * lookup service by rpcServiceName
     *
     * @param rpcRequest rpc service pojo
     * @return service address
     */
    InetSocketAddress lookupService(RPCRequest rpcRequest);
}
