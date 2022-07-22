package com.xxx.rpc.proto;

import lombok.*;

import java.io.Serializable;

/**
 * RPC请求定义
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RPCRequest implements Serializable {

    private static final long serialVersionUID = -5578522771509166762L;

    private String requestId;

    private String interfaceName;

    private String methodName;

    private Class<?>[] paramTypes;

    private Object[] params;

    private String version;

    /**
     * 区分同一接口下不同的实现
     */
    private String group;

    public String getRPCServiceName() {
        return this.interfaceName + this.group + this.version;
    }
}
