package com.xxx.rpc.transport.socket;

import com.xxx.rpc.common.exceptions.RPCException;
import com.xxx.rpc.common.extension.ExtensionLoader;
import com.xxx.rpc.proto.RPCRequest;
import com.xxx.rpc.registry.ServiceDiscovery;
import com.xxx.rpc.transport.RPCRequestTransport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 基于 Socket 传输 RpcRequest
 */
@AllArgsConstructor
@Slf4j
public class SocketRPCClient implements RPCRequestTransport {
    private final ServiceDiscovery serviceDiscovery;

    public SocketRPCClient() {
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
    }

    @Override
    public Object sendRpcRequest(RPCRequest rpcRequest) {
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // Send data to the server through the output stream
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // Read RpcResponse from the input stream
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RPCException("调用服务失败:", e);
        }
    }
}
