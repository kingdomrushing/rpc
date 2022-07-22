package com.xxx.rpc.registry.zk;

import com.xxx.rpc.common.enums.RPCErrorMessageEnum;
import com.xxx.rpc.common.exceptions.RPCException;
import com.xxx.rpc.common.extension.ExtensionLoader;
import com.xxx.rpc.common.utils.CollectionUtil;
import com.xxx.rpc.proto.RPCRequest;
import com.xxx.rpc.registry.ServiceDiscovery;
import com.xxx.rpc.registry.loadbalance.LoadBalance;
import com.xxx.rpc.registry.zk.util.CuratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * service discovery based on zookeeper
 *
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("consistentHashLoadBalance");
    }

    @Override
    public InetSocketAddress lookupService(RPCRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRPCServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (CollectionUtil.isEmpty(serviceUrlList)) {
            throw new RPCException(RPCErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        // load balancing
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
