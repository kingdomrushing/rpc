package com.xxx.rpc.proto;

import com.xxx.rpc.common.enums.RPCResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RPC response
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RPCResponse<T> implements Serializable {

    private static final long serialVersionUID = -4027308452449923876L;

    private String requestId;

    private Integer code;

    private String message;

    private T data;

    public static <T> RPCResponse<T> success(T data, String requestId) {
        RPCResponse<T> response = new RPCResponse<>();
        response.setCode(RPCResponseEnum.SUCCESS.getCode());
        response.setMessage(RPCResponseEnum.SUCCESS.getMessage());
        response.setRequestId(requestId);
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    public static <T> RPCResponse<T> fail(RPCResponseEnum rpcResponseEnum) {
        RPCResponse<T> response = new RPCResponse<>();
        response.setCode(rpcResponseEnum.getCode());
        response.setMessage(rpcResponseEnum.getMessage());
        return response;
    }

}
