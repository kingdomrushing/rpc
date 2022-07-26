package com.xxx.rpc.registry.provider.impl;

import com.xxx.rpc.common.config.RPCServiceConfig;
import com.xxx.rpc.common.constants.RPCConstants;
import com.xxx.rpc.common.enums.RPCErrorMessageEnum;
import com.xxx.rpc.common.exceptions.RPCException;
import com.xxx.rpc.common.extension.ExtensionLoader;
import com.xxx.rpc.registry.ServiceRegistry;
import com.xxx.rpc.registry.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ZkServiceProviderImpl implements ServiceProvider {

    /**
     * key: rpc service name(interface name + version + group)
     * value: service object
     */
    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;

    public ZkServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("zk");
    }

    @Override
    public void addService(RPCServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (registeredService.contains(rpcServiceName)) {
            return;
        }
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        log.info("Add service: {} and interfaces:{}", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (null == service) {
            throw new RPCException(RPCErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(RPCServiceConfig rpcServiceConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            this.addService(rpcServiceConfig);
            serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(),
                    new InetSocketAddress(host, RPCConstants.DEFAULT_SERVER_PORT));
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress", e);
        }
    }

}
