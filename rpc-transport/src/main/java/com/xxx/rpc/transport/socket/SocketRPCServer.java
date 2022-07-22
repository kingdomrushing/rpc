package com.xxx.rpc.transport.socket;

import com.xxx.rpc.common.config.RPCServiceConfig;
import com.xxx.rpc.common.constants.RPCConstants;
import com.xxx.rpc.common.factory.SingletonFactory;
import com.xxx.rpc.common.utils.concurrent.threadpool.ThreadPoolFactoryUtil;
import com.xxx.rpc.registry.provider.ServiceProvider;
import com.xxx.rpc.registry.provider.impl.ZkServiceProviderImpl;
import com.xxx.rpc.transport.config.CustomShutdownHook;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

@Slf4j
public class SocketRPCServer {

    private final ExecutorService threadPool;
    private final ServiceProvider serviceProvider;


    public SocketRPCServer() {
        threadPool = ThreadPoolFactoryUtil.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    public void registerService(RPCServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() {
        try (ServerSocket server = new ServerSocket()) {
            String host = InetAddress.getLocalHost().getHostAddress();
            server.bind(new InetSocketAddress(host, RPCConstants.DEFAULT_SERVER_PORT));
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRPCRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("occur IOException:", e);
        }
    }

}
