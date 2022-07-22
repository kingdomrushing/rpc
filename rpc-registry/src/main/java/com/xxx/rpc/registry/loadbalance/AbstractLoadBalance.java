package com.xxx.rpc.registry.loadbalance;

import com.xxx.rpc.common.utils.CollectionUtil;
import com.xxx.rpc.proto.RPCRequest;

import java.util.List;

/**
 * Abstract class for a load balancing policy
 */
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public String selectServiceAddress(List<String> serviceAddresses, RPCRequest rpcRequest) {
        if (CollectionUtil.isEmpty(serviceAddresses)) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses, rpcRequest);
    }

    protected abstract String doSelect(List<String> serviceAddresses, RPCRequest rpcRequest);

}
