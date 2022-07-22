package com.xxx.rpc.common.exceptions;


import com.xxx.rpc.common.enums.RPCErrorMessageEnum;

public class RPCException extends RuntimeException {
    public RPCException(RPCErrorMessageEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }

    public RPCException(String message, Throwable cause) {
        super(message, cause);
    }

    public RPCException(RPCErrorMessageEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }
}
