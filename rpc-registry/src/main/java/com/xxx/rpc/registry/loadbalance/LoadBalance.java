package com.xxx.rpc.registry.loadbalance;

import com.xxx.rpc.common.annoation.SPI;
import com.xxx.rpc.proto.RPCRequest;

import java.util.List;

/**
 * Interface to the load balancing policy
 */
@SPI
public interface LoadBalance {
    /**
     * Choose one from the list of existing service addresses list
     *
     * @param serviceUrlList Service address list
     * @param rpcRequest
     * @return target service address
     */
    String selectServiceAddress(List<String> serviceUrlList, RPCRequest rpcRequest);
}