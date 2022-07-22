package com.xxx.rpc.registry.zk;

import com.xxx.rpc.common.config.RPCServiceConfig;
import com.xxx.rpc.proto.RPCRequest;
import com.xxx.rpc.registry.ServiceDiscovery;
import com.xxx.rpc.registry.ServiceRegistry;
import com.xxx.rpc.registry.zk.util.CuratorUtils;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZkServiceRegistryImplTest {

    @Test
    void testZkNodePath(){
        String rpcServiceName = "com.xxx.HelloService";
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9333);
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        assertEquals("/my-rpc/com.xxx.HelloService/127.0.0.1:9333", servicePath.toString());
    }

    @Test
    void should_register_service_successful_and_lookup_service_by_service_name() {
        ServiceRegistry zkServiceRegistry = new ZkServiceRegistryImpl();
        InetSocketAddress givenInetSocketAddress = new InetSocketAddress("127.0.0.1", 9333);
        DemoRpcService demoRpcService = new DemoRpcServiceImpl();
        RPCServiceConfig rpcServiceConfig = RPCServiceConfig.builder()
                .group("test2").version("version2").service(demoRpcService).build();
        zkServiceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), givenInetSocketAddress);
        ServiceDiscovery zkServiceDiscovery = new ZkServiceDiscoveryImpl();
        RPCRequest rpcRequest = RPCRequest.builder()
//                .parameters(args)
                .interfaceName(rpcServiceConfig.getServiceName())
//                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceConfig.getGroup())
                .version(rpcServiceConfig.getVersion())
                .build();
        InetSocketAddress acquiredInetSocketAddress = zkServiceDiscovery.lookupService(rpcRequest);
        assertEquals(givenInetSocketAddress.toString(), acquiredInetSocketAddress.toString());
    }


}