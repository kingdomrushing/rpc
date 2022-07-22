package com.xxx.rpc.transport;

import com.xxx.rpc.common.annoation.SPI;
import com.xxx.rpc.proto.RPCRequest;

/**
 * send RpcRequest
 */
@SPI
public interface RPCRequestTransport {

    /**
     * send rpc request to server and get result
     *
     * @param rpcRequest message body
     * @return data from server
     */
    Object sendRpcRequest(RPCRequest rpcRequest);
}
