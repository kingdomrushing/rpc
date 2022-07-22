package com.xxx.rpc.registry.loadbalance.loadbalancer;

import com.xxx.rpc.common.config.RPCServiceConfig;
import com.xxx.rpc.common.extension.ExtensionLoader;
import com.xxx.rpc.proto.RPCRequest;
import com.xxx.rpc.registry.loadbalance.LoadBalance;
import com.xxx.rpc.registry.zk.DemoRpcService;
import com.xxx.rpc.registry.zk.DemoRpcServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class ConsistentHashLoadBalanceTest {

    @Test
    void TestConsistentHashLoadBalance() {
        LoadBalance loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("consistentHashLoadBalance");
        List<String> serviceUrlList = new ArrayList<>(Arrays.asList("127.0.0.1:9997", "127.0.0.1:9998", "127.0.0.1:9999"));

        DemoRpcService demoRpcService = new DemoRpcServiceImpl();
        RPCServiceConfig rpcServiceConfig = RPCServiceConfig.builder()
                .group("test2").version("version2").service(demoRpcService).build();

        RPCRequest rpcRequest = RPCRequest.builder()
                .params(demoRpcService.getClass().getTypeParameters())
                .interfaceName(rpcServiceConfig.getServiceName())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceConfig.getGroup())
                .version(rpcServiceConfig.getVersion())
                .build();
        String userServiceAddress = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        assertEquals("127.0.0.1:9998", userServiceAddress);
    }

}