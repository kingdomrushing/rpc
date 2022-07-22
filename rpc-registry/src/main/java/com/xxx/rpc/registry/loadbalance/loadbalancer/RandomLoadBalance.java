package com.xxx.rpc.registry.loadbalance.loadbalancer;

import com.xxx.rpc.proto.RPCRequest;
import com.xxx.rpc.registry.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

/**
 * Implementation of random load balancing strategy
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> serviceAddresses, RPCRequest rpcRequest) {
        Random random = new Random();
        return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
    }
}
