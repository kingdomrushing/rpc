package com.xxx.rpc.transport.socket;

import com.xxx.rpc.common.factory.SingletonFactory;
import com.xxx.rpc.proto.RPCRequest;
import com.xxx.rpc.proto.RPCResponse;
import com.xxx.rpc.transport.handler.RPCRequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Slf4j
public class SocketRPCRequestHandlerRunnable implements Runnable {
    private final Socket socket;
    private final RPCRequestHandler rpcRequestHandler;


    public SocketRPCRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RPCRequestHandler.class);
    }

    @Override
    public void run() {
        log.info("server handle message from client by thread: [{}]", Thread.currentThread().getName());
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            RPCRequest rpcRequest = (RPCRequest) objectInputStream.readObject();
            Object result = rpcRequestHandler.handle(rpcRequest);
            objectOutputStream.writeObject(RPCResponse.success(result, rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("occur exception:", e);
        }
    }

}
